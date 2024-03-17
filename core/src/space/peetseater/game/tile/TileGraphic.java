package space.peetseater.game.tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.shared.Command;
import space.peetseater.game.states.LinearMovementBehavior;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.tile.states.NotSelected;

public class TileGraphic {
    public static int TILESIZE = 70;
    private final TileType tileType;
    Texture texture;
    TileGraphicState state;
    LinearMovementBehavior positionState;
    MovablePoint movablePoint;

    public TileGraphic(Vector2 position, TileType tileType) {
        this.tileType = tileType;
        this.texture = makeTexture(TileType.colorFor(tileType));
        this.state = NotSelected.getInstance();
        this.movablePoint = new MovablePoint(position);
        this.positionState = new LinearMovementBehavior(this.movablePoint);
    }

    private Texture makeTexture(Color color) {
        Pixmap pixmap = new Pixmap(TILESIZE, TILESIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void render(float delta, SpriteBatch batch) {
        update(delta);
        batch.draw(texture, movablePoint.getPosition().x, movablePoint.getPosition().y);
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
        texture = makeTexture(color.cpy().mul(1, 1, 1, 0.8f));
    }

    public void useNotSelectedTexture() {
        Color color = TileType.colorFor(tileType);
        texture.dispose();
        texture = makeTexture(color);
    }

    public void useMatchedTexture() {
        Color color = TileType.colorFor(tileType);
        texture.dispose();
        texture = makeTexture(color.cpy().mul(1, 1, 1, 0.5f));
    }

    public void setState(TileGraphicState newState) {
        if (newState != null) {
            state = newState;
        }
    }
}
