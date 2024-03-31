package space.peetseater.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.Constants;
import space.peetseater.game.DragEventSubscriber;
import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.commands.SelectCrossSection;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.commands.MoveTowards;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.tile.commands.DeselectTile;
import space.peetseater.game.tile.commands.IncludeInMatch;
import space.peetseater.game.tile.commands.SelectTile;

import java.util.LinkedList;
import java.util.Queue;

import static space.peetseater.game.states.Match3GameState.STATE.IDLE;
import static space.peetseater.game.states.Match3GameState.STATE.IN_PROGRESS;

public class Match3GameState implements DragEventSubscriber {

    private final BoardGraphic boardGraphic;
    private final GameGrid<TileType> gameGrid;
    private STATE state;

    enum STATE {
        IDLE,
        IN_PROGRESS,
        HAS_MATCHES
    }

    Queue<Command> commands;

    TileGraphic selected;

    SelectCrossSection crossSection;

    public Match3GameState(BoardGraphic boardGraphic, GameGrid<TileType> gameGrid) {
        this.boardGraphic = boardGraphic;
        this.gameGrid = gameGrid;
        this.state = IDLE;
        this.commands = new LinkedList<>();
        this.selected = null;
        this.crossSection = null;
    }

    public void update(float delta) {
        while (!commands.isEmpty()) {
            Command command = commands.remove();
            command.execute();
        }
    }

    protected boolean pointInBoard(float gameX, float gameY) {
        return this.boardGraphic.pointInBounds(gameX, gameY);
    }

    @Override
    public void onDragStart(float gameX, float gameY) {
        if (!pointInBoard(gameX, gameY)) {
            return;
        }
        if (state != IDLE) {
            return;
        }
        state = IN_PROGRESS;
        int row = this.boardGraphic.gameYToRow(gameY);
        int column = this.boardGraphic.gameXToColumn(gameX);
        this.selected = this.boardGraphic.getTile(row, column);
        this.crossSection =new SelectCrossSection(boardGraphic, row, column);
        commands.add(crossSection);
        commands.add(new IncludeInMatch(selected));
    }

    @Override
    public void onDrag(float gameX, float gameY) {
        if (!pointInBoard(gameX, gameY)) {
            return;
        }

        if (state != IN_PROGRESS) {
            return;
        }

        Gdx.app.log("onDrag", "? " + gameX + "," + gameY);
        float offsetByHalfX = gameX - Constants.TILE_UNIT_WIDTH / 2;
        float offsetByHalfY = gameY - Constants.TILE_UNIT_HEIGHT / 2;
        commands.add(new MoveTowards(new Vector2(offsetByHalfX, offsetByHalfY), selected.getMovablePoint()));
    }

    @Override
    public void onDragEnd(float gameX, float gameY) {
        if (!pointInBoard(gameX, gameY)) {
            return;
        }
        int row = this.boardGraphic.gameYToRow(gameY);
        int column = this.boardGraphic.gameXToColumn(gameX);
        this.commands.add(new DeselectTile(selected));
        // TODO handle matching
        commands.add(crossSection.undoCommand());
        this.selected = null;
        this.crossSection = null;
        state = IDLE;
    }

    @Override
    public void onLeftClick(float gameX, float gameY) {
        // Do nothing.
    }
}
