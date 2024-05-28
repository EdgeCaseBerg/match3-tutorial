package space.peetseater.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import space.peetseater.game.Match3Assets;

import java.util.HashSet;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class Button implements MenuEventSubscriber {
    protected float xActiveOffset;
    protected float yActiveOffset;
    protected HashSet<ButtonListener> listeners;

    static enum ButtonState {
        IDLE,
        HOVER,
        ACTIVE
    }

    private final String text;
    private ButtonState buttonState;
    private final Vector2 minSize;
    private final Vector2 position;
    private final Match3Assets match3Assets;
    protected NinePatch inactive;
    protected NinePatch hovering;
    protected NinePatch active;

    public Button(String text, Vector2 position, Vector2 minimumSize, Match3Assets match3Assets) {
        this.text = text;
        this.buttonState = ButtonState.IDLE;
        this.position = position;
        this.minSize = minimumSize;
        this.match3Assets = match3Assets;
        this.inactive = this.hovering = this.active = null;
        this.xActiveOffset = 0;
        this.yActiveOffset = -0.1f;
        this.listeners = new HashSet<>();
    }

    public void setHorizontalActiveOffset(float xOffsetWhenPressed) {
        this.xActiveOffset = xOffsetWhenPressed;
    }

    public void setVerticalActiveOffset(float yOffsetWhenPressed) {
        this.yActiveOffset = yOffsetWhenPressed;
    }

    protected void make9Patch() {
        Texture texture = match3Assets.getButton9PatchTexture();
        TextureRegion[][] regions = TextureRegion.split(texture, 32, 32);

        this.inactive = new NinePatch(regions[0][0], 8, 8, 8, 8);
        inactive.scale(GAME_WIDTH / (float) Gdx.graphics.getWidth(), GAME_HEIGHT /(float) Gdx.graphics.getHeight());

        this.hovering = new NinePatch(regions[0][1], 8, 8, 8, 8);
        hovering.scale(GAME_WIDTH / (float) Gdx.graphics.getWidth(), GAME_HEIGHT /(float) Gdx.graphics.getHeight());

        this.active = new NinePatch(regions[0][2], 8, 8, 8, 8);
        active.scale(GAME_WIDTH / (float) Gdx.graphics.getWidth(), GAME_HEIGHT /(float) Gdx.graphics.getHeight());
    }

    public void render(float delta, SpriteBatch batch, BitmapFont font) {
        if (inactive == null) {
            make9Patch();
        }

        float yOffset = 0;
        float xOffset = 0;
        Color batchColor = batch.getColor().cpy();
        switch (buttonState) {
            case IDLE:
                batch.setColor(batchColor.r, batchColor.g, batchColor.b, 0.8f);
                inactive.draw(batch, position.x, position.y, minSize.x, minSize.y);
                break;
            case HOVER:
                batch.setColor(batchColor.r, batchColor.g, batchColor.b, batchColor.a);
                hovering.draw(batch, position.x, position.y, minSize.x, minSize.y);
                break;
            case ACTIVE:
                yOffset = yActiveOffset;
                xOffset = xActiveOffset;
                batch.setColor(batchColor.r, batchColor.g, batchColor.b, batchColor.a);
                active.draw(batch, position.x + xOffset, position.y + yOffset, minSize.x, minSize.y);
                break;
        }
        batch.setColor(batchColor);

        // Textures are draw from the bottom left corner, but font is from the top left corner
        // So we have to compute where the font's y value is by flipping and shifting
        float fontYPosition = position.y + minSize.y - inactive.getPadTop() - inactive.getPadBottom() + yOffset;
        font.draw(batch, text, position.x + inactive.getPadLeft() + xOffset, fontYPosition,  minSize.x, Align.center, false);
    }

    public boolean contains(Vector2 point) {
        boolean insideX = position.x <= point.x && point.x <= position.x + minSize.x;
        boolean insideY = position.y <= point.y && point.y <= position.y + minSize.y;
        return insideY && insideX;
    }

    public boolean onMouseMove(Vector2 point) {
        if (contains(point)) {
            if (buttonState == ButtonState.IDLE) {
                buttonState = ButtonState.HOVER;
                return true;
            }
        } else {
            buttonState = ButtonState.IDLE;
        }
        return false;
    }

    public boolean onTouchDown() {
        switch (buttonState) {
            case IDLE:
            case ACTIVE:
                break;
            case HOVER:
                buttonState = ButtonState.ACTIVE;
                for (ButtonListener listener : listeners) {
                    listener.buttonClicked(this);
                }
                return true;
        }
        return false;
    }

    public boolean onTouchUp() {
        switch (buttonState) {
            case IDLE:
            case HOVER:
                break;
            case ACTIVE:
                buttonState = ButtonState.HOVER;
                return true;
        }
        return false;
    }

    public void addButtonListener(ButtonListener buttonListener) {
        listeners.add(buttonListener);
    }

    public String getText() {
        return text;
    }
}
