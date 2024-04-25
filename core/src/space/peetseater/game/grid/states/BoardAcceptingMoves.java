package space.peetseater.game.grid.states;

import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.Constants;
import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.commands.HighlightMatchesOnBoard;
import space.peetseater.game.grid.commands.RepositionCrossSection;
import space.peetseater.game.grid.commands.SelectCrossSection;
import space.peetseater.game.grid.commands.ShiftToken;
import space.peetseater.game.grid.match.CrossSectionTileMatcher;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.commands.MoveTowards;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.tile.commands.DeselectTile;
import space.peetseater.game.tile.commands.IncludeInMatch;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BoardAcceptingMoves implements BoardState {
    private final BoardGraphic boardGraphic;
    private final GameGrid<TileType> gameGrid;
    private final LinkedList<ShiftToken> dragMoves;
    private final Match3GameState match3GameState;
    private GridSpace<TileGraphic> lastMove;
    Queue<Command> commands;

    TileGraphic selected;

    SelectCrossSection crossSection;
    private boolean hasMatches;

    public BoardAcceptingMoves(Match3GameState match3GameState) {
        this.match3GameState = match3GameState;
        this.boardGraphic = match3GameState.getBoardGraphic();
        this.gameGrid = match3GameState.getGameGrid();
        this.commands = new LinkedList<>();
        this.selected = null;
        this.crossSection = null;
        this.dragMoves = new LinkedList<>();
        this.lastMove = null;
        this.hasMatches = false;
    }

    @Override
    public void onDragStart(float gameX, float gameY) {
        int row = this.boardGraphic.gameYToRow(gameY);
        int column = this.boardGraphic.gameXToColumn(gameX);
        this.selected = this.boardGraphic.getTile(row, column);
        this.crossSection = new SelectCrossSection(boardGraphic, row, column);
        this.commands.add(crossSection);
        this.commands.add(new IncludeInMatch(selected));
    }

    @Override
    public void onDrag(float gameX, float gameY) {
        int row = this.boardGraphic.gameYToRow(gameY);
        int column = this.boardGraphic.gameXToColumn(gameX);

        if (crossSection == null) {
            return;
        }

        int startRow = crossSection.getRow();
        int startColumn = crossSection.getColumn();

        if (row == startRow || column == startColumn) {
            // The user is hovering their mouse inside of the cross section
            int startMovesFromR = startRow;
            int startMovesFromC = startColumn;
            if (lastMove != null) {
                startMovesFromR = lastMove.getRow();
                startMovesFromC = lastMove.getColumn();
            }
            List<ShiftToken> newMoves = boardGraphic.gameGrid.getShiftsToMoveFromStartToEnd(startMovesFromR, startMovesFromC, row, column);
            commands.addAll(newMoves);
            dragMoves.addAll(newMoves);

            if (!newMoves.isEmpty()) {
                // DeSelect any matches from the last update so we have a fresh slate
                List<Match<TileGraphic>> matches = (new CrossSectionTileMatcher<>(startRow, startColumn, boardGraphic.gameGrid)).findMatches();
                for (Match<TileGraphic> match : matches) {
                    for (TileGraphic tileGraphic : match.getValues()) {
                        commands.add(new DeselectTile(tileGraphic));
                    }
                }
                // Re-select the cross section so we don't lose our allowed moves
                commands.add(crossSection);
                commands.add(new HighlightMatchesOnBoard(boardGraphic.gameGrid, startRow, startColumn));
            }

            lastMove = boardGraphic.gameGrid.getTile(row, column);
        } else {
            // The user is hovering their mouse outside of the allowed move locations
            while (!dragMoves.isEmpty()) {
                commands.add(dragMoves.removeLast());
            }
            lastMove = null;
        }

        commands.add(new RepositionCrossSection(boardGraphic, startRow, startColumn));

        // Move Selected tile towards mouse
        float offsetByHalfX = gameX - Constants.TILE_UNIT_WIDTH / 2;
        float offsetByHalfY = gameY - Constants.TILE_UNIT_HEIGHT / 2;
        commands.add(new MoveTowards(new Vector2(offsetByHalfX, offsetByHalfY), selected.getMovablePoint()));
    }

    @Override
    public void onDragEnd(float gameX, float gameY) {
        while (!dragMoves.isEmpty()) {
            this.commands.add(dragMoves.removeLast());
        }

        // Can remove this at some point. For now it's a good debug.
        Iterator<GridSpace<TileType>> tileTypeIter = gameGrid.iterator();
        Iterator<GridSpace<TileGraphic>> tileGraphicIter = boardGraphic.gameGrid.iterator();
        while (tileGraphicIter.hasNext()) {
            TileType t = tileGraphicIter.next().getValue().getTileType();
            TileType tg = tileTypeIter.next().getValue();
            assert t == tg;
        }

        if (crossSection == null) {
            return;
        }

        this.commands.add(crossSection.undoCommand());
        this.commands.add(new DeselectTile(selected));

        if (!this.boardGraphic.pointInBounds(gameX, gameY)) {
            this.commands.add(new RepositionCrossSection(boardGraphic, crossSection.getRow(), crossSection.getColumn()));
            this.selected = null;
            this.crossSection = null;
            return;
        }

        int row = this.boardGraphic.gameYToRow(gameY);
        int column = this.boardGraphic.gameXToColumn(gameX);

        List<ShiftToken> moves = this.gameGrid.getShiftsToMoveFromStartToEnd(crossSection.getRow(), crossSection.getColumn(), row, column);
        this.hasMatches = gameGrid.testIfMovesValid(moves, new CrossSectionTileMatcher<>(crossSection.getRow(), crossSection.getColumn(), gameGrid));
        if (this.hasMatches) {
            this.commands.addAll(moves);
            for (ShiftToken shiftToken : moves) {
                this.commands.add(
                        new ShiftToken(shiftToken.startRow, shiftToken.startColumn, shiftToken.moveDirection, boardGraphic.gameGrid)
                );
            }
            this.commands.add(new HighlightMatchesOnBoard(boardGraphic.gameGrid, crossSection.getRow(), crossSection.getColumn()));
        }

        this.commands.add(new RepositionCrossSection(boardGraphic, crossSection.getRow(), crossSection.getColumn()));

        this.selected = null;
        this.crossSection = null;
        this.lastMove = null;
    }

    @Override
    public void onLeftClick(float gameX, float gameY) {
        while (!dragMoves.isEmpty()) {
            this.commands.add(dragMoves.removeLast());
        }

        if (crossSection == null) {
            return;
        }

        this.commands.add(crossSection.undoCommand());
        this.commands.add(new DeselectTile(selected));
        this.commands.add(new RepositionCrossSection(boardGraphic, crossSection.getRow(), crossSection.getColumn()));
        this.selected = null;
        this.crossSection = null;
        this.lastMove = null;
    }

    @Override
    public void onEnterState(Match3GameState match3GameState) {
        this.selected = null;
        this.crossSection = null;
        this.lastMove = null;
        this.hasMatches = false;
    }

    @Override
    public void onExitState(Match3GameState match3GameState) {
        while (!dragMoves.isEmpty()) {
            this.commands.add(dragMoves.removeLast());
        }
        this.selected = null;
        this.crossSection = null;
        this.lastMove = null;
        this.hasMatches = false;
    }

    @Override
    public BoardState update(float delta) {
        while (!commands.isEmpty()) {
            Command command = commands.remove();
            command.execute();
        }

        if(hasMatches) {
            return new ProcessingMatches(match3GameState);
        }
        return this;
    }
}
