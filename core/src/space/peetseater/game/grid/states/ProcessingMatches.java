package space.peetseater.game.grid.states;

import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.commands.ApplyGravityToColumn;
import space.peetseater.game.grid.commands.ClearGridSpace;
import space.peetseater.game.grid.commands.DropTileToSpace;
import space.peetseater.game.grid.commands.RepositionColumn;
import space.peetseater.game.grid.match.FullGridTileMatcher;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.grid.match.MatchEventPublisher;
import space.peetseater.game.grid.match.MatchSubscriber;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;

import java.util.*;

public class ProcessingMatches implements BoardState, MatchEventPublisher<TileType> {

    private final BoardGraphic boardGraphic;
    private final GameGrid<TileType> gameGrid;
    private final LinkedList<Command> commands;
    private final HashSet<MatchSubscriber<TileType>> subscribers;
    private final Match3GameState match3GameState;

    private boolean isDoneProcessing;

    public ProcessingMatches(Match3GameState match3GameState) {
        this.match3GameState = match3GameState;
        this.boardGraphic = match3GameState.getBoardGraphic();
        this.gameGrid = match3GameState.getGameGrid();
        this.commands = new LinkedList<>();
        this.isDoneProcessing = false;
        this.subscribers = new HashSet<>(1);
    }

    protected void processMatches() {
        // Prevent processing further matches until the board settles
        // Note we could probably optimize this by observing the movables + tilegraphics
        // and then setting the flag based on how many tilegraphics have been told to move
        // and decrement a counter when they finish and when it's 0 then the board is
        // settled.
        boolean tilesMoving = false;
        Iterator<GridSpace<TileGraphic>> iter2 = boardGraphic.gameGrid.iterator();
        while(iter2.hasNext()) {
            GridSpace<TileGraphic> tileGraphicGridSpace = iter2.next();
            if (tileGraphicGridSpace.isFilled()) {
                Vector2 position = tileGraphicGridSpace.getValue().getMovablePoint().getPosition();
                Vector2 destination = tileGraphicGridSpace.getValue().getMovablePoint().getDestination();
                tilesMoving = !(position.equals(destination) || destination == null);
            }
            if (tilesMoving) {
                break;
            }
        }
        if (tilesMoving) {
            return;
        }

        // Matches
        FullGridTileMatcher<TileType> tileMatcher = new FullGridTileMatcher<>(gameGrid);
        List<Match<TileType>> newMatches = tileMatcher.findMatches();
        this.isDoneProcessing = isDoneProcessing || newMatches.isEmpty();

        // Check for empty spaces that need to be filled, this will basically be ran on the next frame after the above commands in the queue have been processed.
        Iterator<GridSpace<TileGraphic>> iter = boardGraphic.gameGrid.iterator();
        while(iter.hasNext()) {
            GridSpace<TileGraphic> tileGraphicGridSpace = iter.next();
            if (tileGraphicGridSpace.getValue() == null) {
                isDoneProcessing = false;
                this.commands.add(new DropTileToSpace(match3GameState, tileGraphicGridSpace.getRow(), tileGraphicGridSpace.getColumn()));
            } else {
                MovablePoint movablePoint = tileGraphicGridSpace.getValue().getMovablePoint();
                isDoneProcessing = isDoneProcessing && newMatches.isEmpty() && (movablePoint.getPosition().equals(movablePoint.getDestination()) || movablePoint.getDestination() == null);
            }
        }

        if (!newMatches.isEmpty()) {
            for (MatchSubscriber<TileType> matchSubscriber : subscribers) {
                // TODO: refactor to use a match event class or something so we can stop passing boardgraphic to score graphic
                matchSubscriber.onMatches(newMatches);
            }
        }

        Set<Integer> columnsToApplyGravityTo = new HashSet<>();
        for (Match<TileType> match : newMatches) {
            for (GridSpace space : match.getSpaces()) {
                columnsToApplyGravityTo.add(space.getColumn());
            }
        }

        List<ClearGridSpace> clears = gameGrid.getClearCommandsForMatches(newMatches);
        this.commands.addAll(clears);

        for (Integer column : columnsToApplyGravityTo) {
            this.commands.add(new ApplyGravityToColumn(column, gameGrid));
        }

        FullGridTileMatcher<TileGraphic> tileMatcher2 = new FullGridTileMatcher<>(boardGraphic.gameGrid);
        List<Match<TileGraphic>> graphicalMatches = tileMatcher2.findMatches();

        List<ClearGridSpace> graphicClears = boardGraphic.gameGrid.getClearCommandsForMatches(graphicalMatches);
        this.commands.addAll(graphicClears);
        for (Integer column : columnsToApplyGravityTo) {
            this.commands.add(new ApplyGravityToColumn(column, boardGraphic.gameGrid));
            this.commands.add(new RepositionColumn(column, boardGraphic));
        }
    }

    @Override
    public void onEnterState(Match3GameState match3GameState) {
        addSubscriber(match3GameState);
    }

    @Override
    public void onExitState(Match3GameState match3GameState) {
        removeSubscriber(match3GameState);
    }

    @Override
    public BoardState update(float delta) {
        if (commands.isEmpty()) {
            processMatches();
        }
        while (!this.commands.isEmpty()) {
            Command command = this.commands.remove();
            command.execute();
        }
        if (isDoneProcessing) {
            return new BoardAcceptingMoves(match3GameState);
        }
        return this;
    }

    @Override
    public void onDragStart(float gameX, float gameY) {
        // Do nothing. The user needs to wait until we're done
        // before we do anything.
    }

    @Override
    public void onDrag(float gameX, float gameY) {
        // Do nothing. The user needs to wait until we're done
        // before we do anything.
    }

    @Override
    public void onDragEnd(float gameX, float gameY) {
        // Do nothing. The user needs to wait until we're done
        // before we do anything.
    }

    @Override
    public void onLeftClick(float gameX, float gameY) {
        // Do nothing. The user needs to wait until we're done
        // before we do anything.
    }

    @Override
    public void addSubscriber(MatchSubscriber<TileType> matchSubscriber) {
        this.subscribers.add(matchSubscriber);
    }

    @Override
    public void removeSubscriber(MatchSubscriber<TileType> matchSubscriber) {
        this.subscribers.remove(matchSubscriber);
    }
}
