package space.peetseater.game.grid;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import space.peetseater.game.Constants;
import space.peetseater.game.GameSettings;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.screens.menu.BackgroundPanel;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.shared.commands.MoveTowards;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.tile.commands.DeselectTile;
import space.peetseater.game.tile.commands.SelectTile;

import java.util.List;

import static space.peetseater.game.Constants.*;

public class BoardManager {
    public final Match3Assets match3Assets;
    protected MovablePoint movablePoint;
    private final BackgroundPanel bg;
    public final GameGrid<TileGraphic> gameGrid;
    public BoardManager(final Vector2 position, GameGrid<TileType> sourceOfTruth, Match3Assets match3Assets) {
        this.movablePoint = new MovablePoint(position);
        this.match3Assets = match3Assets;
        this.bg = new BackgroundPanel(position, new Vector2(BOARD_UNIT_WIDTH, BOARD_UNIT_HEIGHT), match3Assets);
        this.gameGrid = new GameGrid<>(sourceOfTruth.getWidth(), sourceOfTruth.getHeight());
        initializeGrid(sourceOfTruth);
    }

    protected void initializeGrid(GameGrid<TileType> sourceOfTruth) {
        for (GridSpace<TileGraphic> tileGraphicGameGrid : gameGrid) {
            GridSpace<TileType> tokenSpace = sourceOfTruth.getTile(tileGraphicGameGrid.getRow(), tileGraphicGameGrid.getColumn());

            float ty = screenYFromGridRow(tileGraphicGameGrid.getRow());
            float tx = screenXFromGridColumn(tileGraphicGameGrid.getColumn());
            Vector2 offsetPosition = new Vector2(tx, ty);

            TileGraphic tileGraphic = new TileGraphic(offsetPosition, tokenSpace.getValue(), match3Assets);
            tileGraphicGameGrid.setValue(tileGraphic);
        }
    }

    public void playSelectSFX() {
        Sound selectSFX = match3Assets.getSelectSFX();
        selectSFX.stop();
        selectSFX.play(GameSettings.getInstance().getSfxVolume());
    }

    public float screenYFromGridRow(int row) {
        float gutter = Constants.BOARD_UNIT_GUTTER;
        return this.movablePoint.getPosition().y + (row * (1 + gutter) + gutter);
    }

    public float screenXFromGridColumn(int column) {
        float gutter = Constants.BOARD_UNIT_GUTTER;
        return this.movablePoint.getPosition().x + (column * (1 + gutter) + gutter);
    }

    public void render(float delta, SpriteBatch batch) {
        update(delta);
        bg.render(delta, batch);
        for (GridSpace<TileGraphic> tileGraphicGridSpace : gameGrid) {
            if (tileGraphicGridSpace.isFilled()) {
                TileGraphic tileGraphic = tileGraphicGridSpace.getValue();
                tileGraphic.render(delta, batch);
            }
        }
    }

    public void update(float delta) {
        for (GridSpace<TileGraphic> tileGraphicGridSpace : gameGrid) {
            if (tileGraphicGridSpace.isFilled()) {
                tileGraphicGridSpace.getValue().update(delta);
            }
        }
    }

    public void replaceTile(int row, int column, TileType tileType) {
        GridSpace<TileGraphic> space = this.gameGrid.getTile(row, column);
        Vector2 position;
        float aboveBoard = BOARD_UNIT_HEIGHT + 2f;
        float ty = screenYFromGridRow(space.getRow());
        float tx = screenXFromGridColumn(space.getColumn());

        if (space.isFilled()) {
             position = space.getValue().getMovablePointPosition();
        } else {
            position = new Vector2(tx, aboveBoard);
        }
        space.setValue(new TileGraphic(position, tileType, match3Assets));
        space.getValue().handleCommand(new MoveTowards(new Vector2(tx, ty)));
    }

    public boolean pointInBounds(float gameX, float gameY) {
        Vector2 origin = movablePoint.getPosition();
        boolean inXRange = origin.x + BOARD_UNIT_GUTTER <= gameX && gameX <= origin.x + BOARD_UNIT_WIDTH - BOARD_UNIT_GUTTER;
        boolean inYRange = origin.y + BOARD_UNIT_GUTTER <= gameY && gameY <= origin.y + BOARD_UNIT_HEIGHT - BOARD_UNIT_GUTTER;
        return inXRange && inYRange;
    }

    public int gameXToColumn(float gameX) {
        float originTileX = movablePoint.getPosition().x + BOARD_UNIT_GUTTER;
        int tileDistance = (int) (gameX - originTileX);
        int columnAdjustedForGutters = (int) (gameX - originTileX -  tileDistance * BOARD_UNIT_GUTTER);
        return MathUtils.clamp(columnAdjustedForGutters, 0, gameGrid.getWidth() - 1);
    }

    public int gameYToRow(float gameY) {
        float originTileY = movablePoint.getPosition().y + BOARD_UNIT_GUTTER;
        int tileDistance = (int) (gameY - originTileY);
        int columnAdjustedForGutters = (int) (gameY - originTileY -  tileDistance * BOARD_UNIT_GUTTER);
        return MathUtils.clamp(columnAdjustedForGutters, 0, gameGrid.getHeight() - 1);
    }

    public void selectCrossSection(int row, int column) {
        List<GridSpace<TileGraphic>> spacesInRow = gameGrid.getRow(row);
        List<GridSpace<TileGraphic>> spacesInColumn = gameGrid.getColumn(column);
        for (GridSpace<TileGraphic> space : spacesInRow) {
            space.getValue().handleCommand(new SelectTile(space.getValue()));
        }
        for (GridSpace<TileGraphic> space : spacesInColumn) {
            space.getValue().handleCommand(new SelectTile(space.getValue()));
        }
    }

    public void deselectCrossSection(int row, int column) {
        List<GridSpace<TileGraphic>> spacesInRow = gameGrid.getRow(row);
        List<GridSpace<TileGraphic>> spacesInColumn = gameGrid.getColumn(column);
        for (GridSpace<TileGraphic> space : spacesInRow) {
            space.getValue().handleCommand(new DeselectTile(space.getValue()));
        }
        for (GridSpace<TileGraphic> space : spacesInColumn) {
            space.getValue().handleCommand(new DeselectTile(space.getValue()));
        }
    }

    public void repositionCrossSection(int row, int column) {
        List<GridSpace<TileGraphic>> spacesInRow = gameGrid.getRow(row);
        List<GridSpace<TileGraphic>> spacesInColumn = gameGrid.getColumn(column);
        for (GridSpace<TileGraphic> space : spacesInRow) {
            Vector2 destination = new Vector2(
                screenXFromGridColumn(space.getColumn()),
                screenYFromGridRow(space.getRow())
            );
            space.getValue().handleCommand(new MoveTowards(destination, space.getValue().getMovablePoint()));
        }
        for (GridSpace<TileGraphic> space : spacesInColumn) {
            Vector2 destination = new Vector2(
                screenXFromGridColumn(space.getColumn()),
                screenYFromGridRow(space.getRow())
            );
            space.getValue().handleCommand(new MoveTowards(destination, space.getValue().getMovablePoint()));
        }
    }

    public void repositionColumn(int column) {
        List<GridSpace<TileGraphic>> spacesInColumn = gameGrid.getColumn(column);
        for (GridSpace<TileGraphic> space : spacesInColumn) {
            Vector2 destination = new Vector2(
                    screenXFromGridColumn(space.getColumn()),
                    screenYFromGridRow(space.getRow())
            );
            if (space.getValue() != null) {
                space.getValue().handleCommand(new MoveTowards(destination, space.getValue().getMovablePoint()));
            }
        }
    }

    public TileGraphic getTile(int row, int column) {
        return this.gameGrid.getTile(row, column).getValue();
    }
}
