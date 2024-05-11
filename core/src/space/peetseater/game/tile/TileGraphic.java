package space.peetseater.game.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.shared.SparkleGraphic;
import space.peetseater.game.states.LinearMovementBehavior;
import space.peetseater.game.tile.states.NotSelected;

import java.util.Objects;

import static space.peetseater.game.Constants.TILE_UNIT_HEIGHT;
import static space.peetseater.game.Constants.TILE_UNIT_WIDTH;
import static space.peetseater.game.Match3Assets.*;

public class TileGraphic implements Disposable {
    private final TileType tileType;
    private final TextureRegion idleTextureRegion;
    private final TextureRegion selectedTextureRegion;
    private final Match3Assets match3Assets;
    TextureRegion texture;
    TileGraphicState state;
    LinearMovementBehavior positionState;
    MovablePoint movablePoint;
    SparkleGraphic sparkleGraphic;
    public TileGraphic(Vector2 position, TileType tileType, Match3Assets match3Assets) {
        this.tileType = tileType;
        Texture sheet = match3Assets.getTokenSheetTexture();
        sparkleGraphic = new SparkleGraphic(position, match3Assets, TILE_UNIT_HEIGHT);
        int y = match3Assets.getStartYOfTokenInSheet(tileType);
        this.idleTextureRegion = new TextureRegion(sheet, TOKEN_SPRITE_IDLE_START, y, TOKEN_SPRITE_PIXEL_WIDTH, TOKEN_SPRITE_PIXEL_HEIGHT);
        this.selectedTextureRegion = new TextureRegion(sheet, TOKEN_SPRITE_SELECTED_START, y, TOKEN_SPRITE_PIXEL_WIDTH, TOKEN_SPRITE_PIXEL_HEIGHT);
        this.texture = idleTextureRegion;
        this.state = NotSelected.getInstance();
        this.movablePoint = new MovablePoint(position);
        this.positionState = new LinearMovementBehavior(this.movablePoint);
        this.match3Assets = match3Assets;
    }

    public void render(float delta, SpriteBatch batch) {
        update(delta);
        batch.draw(texture, movablePoint.getPosition().x, movablePoint.getPosition().y, TILE_UNIT_WIDTH, TILE_UNIT_HEIGHT);
        sparkleGraphic.render(delta, batch);
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
        texture = selectedTextureRegion;
        sparkleGraphic.setEnabled(false);
    }

    public void useNotSelectedTexture() {
        texture = idleTextureRegion;
        sparkleGraphic.setEnabled(false);
    }

    public void useMatchedTexture() {
        texture = selectedTextureRegion;
        sparkleGraphic.setEnabled(true);
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

    @Override
    public void dispose() {
        match3Assets.unloadTokenSheetTexture();
    }
}
