package space.peetseater.game.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.TestTexture;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;

import static space.peetseater.game.Constants.BOARD_UNIT_HEIGHT;
import static space.peetseater.game.Constants.BOARD_UNIT_WIDTH;

public class BoardGraphic {
    protected MovablePoint movablePoint;
    private final Texture texture;
    protected final GameGrid<TileGraphic> gameGrid;
    public BoardGraphic(final Vector2 position, GameGrid<TileType> sourceOfTruth) {
        this.movablePoint = new MovablePoint(position);
        this.texture = TestTexture.makeTexture(new Color(1, 1, 1, 0.5f));
        this.gameGrid = new GameGrid<>(sourceOfTruth.getWidth(), sourceOfTruth.getHeight());
        initializeGrid(sourceOfTruth);
    }

    protected void initializeGrid(GameGrid<TileType> sourceOfTruth) {
        float gutter = 0.2f;
        for (GridSpace<TileGraphic> tileGraphicGameGrid : gameGrid) {
            GridSpace<TileType> tokenSpace = sourceOfTruth.getTile(tileGraphicGameGrid.getRow(), tileGraphicGameGrid.getColumn());

            float ty = tileGraphicGameGrid.getRow() * (1 + gutter) + gutter;
            float tx = tileGraphicGameGrid.getColumn() * (1 + gutter) + gutter;
            Vector2 offsetPosition = this.movablePoint.getPosition().cpy().add(tx, ty);

            TileGraphic tileGraphic = new TileGraphic(offsetPosition, tokenSpace.getValue());
            tileGraphicGameGrid.setValue(tileGraphic);
        }
    }

    public void render(float delta, SpriteBatch batch) {
        update(delta);
        batch.draw(texture, movablePoint.getPosition().x, movablePoint.getPosition().y, BOARD_UNIT_WIDTH, BOARD_UNIT_HEIGHT);
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

}
