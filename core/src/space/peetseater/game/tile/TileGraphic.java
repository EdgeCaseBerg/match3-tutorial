package space.peetseater.game.tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.TestTexture;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.states.LinearMovementBehavior;
import space.peetseater.game.tile.states.NotSelected;

import java.util.Objects;

import static space.peetseater.game.Constants.TILE_UNIT_HEIGHT;
import static space.peetseater.game.Constants.TILE_UNIT_WIDTH;

public class TileGraphic {
    private final TileType tileType;
    Texture texture;
    TileGraphicState state;
    LinearMovementBehavior positionState;
    MovablePoint movablePoint;

    public TileGraphic(Vector2 position, TileType tileType) {
        this.tileType = tileType;
        this.texture = TestTexture.makeTexture(TileType.colorFor(tileType));
        this.state = NotSelected.getInstance();
        this.movablePoint = new MovablePoint(position);
        this.positionState = new LinearMovementBehavior(this.movablePoint);
    }

    public void render(float delta, SpriteBatch batch) {
        update(delta);
        batch.draw(texture, movablePoint.getPosition().x, movablePoint.getPosition().y, TILE_UNIT_WIDTH, TILE_UNIT_HEIGHT);
    }

    public void update(float delta) {
        positionState.update(delta);
        state.update(delta);
    }

    public void handleCommand(Command command) {
        Command handledCommand = state.handleCommand(command);
        Command positionCommand = positionState.handleCommand(command);
        if (null != handledCommand) {
            command.execute();
        }
        if (null != positionCommand) {
            positionCommand.execute();
        }
    }

    public void useSelectedTexture() {
        Color color = TileType.colorFor(tileType);
        texture.dispose();
        texture = TestTexture.makeTexture(color.cpy().mul(1, 1, 1, 0.8f));
    }

    public void useNotSelectedTexture() {
        Color color = TileType.colorFor(tileType);
        texture.dispose();
        texture = TestTexture.makeTexture(color);
    }

    public void useMatchedTexture() {
        Color color = TileType.colorFor(tileType);
        texture.dispose();
        texture = TestTexture.makeTexture(color.cpy().mul(1, 1, 1, 0.5f));
    }

    public void setState(TileGraphicState newState) {
        if (newState != null) {
            state = newState;
        }
    }

    public Vector2 getMovablePointPosition() {
        return this.movablePoint.getPosition().cpy();
    }

    public MovablePoint getMovablePoint() {
        return this.movablePoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TileGraphic that = (TileGraphic) o;
        return tileType == that.tileType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tileType, texture, state, positionState, movablePoint);
    }

    public TileType getTileType() {
        return this.tileType;
    }
}
