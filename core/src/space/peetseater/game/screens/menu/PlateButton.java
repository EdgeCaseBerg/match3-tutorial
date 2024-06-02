package space.peetseater.game.screens.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.tile.TileType;

import static space.peetseater.game.Match3Assets.*;

public class PlateButton extends ToggleButton {

    TextureRegion emptyPlate;
    TextureRegion filledPlate;

    public PlateButton(String text, Vector2 position, Vector2 minimumSize, Match3Assets match3Assets) {
        super(text, text, position, minimumSize, match3Assets);
    }

    private TextureRegion getEmptyTexture() {
        Texture sheet = match3Assets.getTokenSheetTexture();
        if (this.emptyPlate == null) {
            int y = match3Assets.getStartYOfTokenInSheet(TileType.Negative);
            this.emptyPlate = new TextureRegion(sheet, TOKEN_SPRITE_IDLE_START, y, TOKEN_SPRITE_PIXEL_WIDTH, TOKEN_SPRITE_PIXEL_HEIGHT);
        }
        return this.emptyPlate;
    }

    private TextureRegion getFilledTexture() {
        Texture sheet = match3Assets.getTokenSheetTexture();
        if (this.filledPlate == null) {
            int y = match3Assets.getStartYOfTokenInSheet(TileType.HighValue);
            this.filledPlate = new TextureRegion(sheet, TOKEN_SPRITE_IDLE_START, y, TOKEN_SPRITE_PIXEL_WIDTH, TOKEN_SPRITE_PIXEL_HEIGHT);
        }
        return this.filledPlate;
    }

    @Override
    public void render(float delta, SpriteBatch batch, BitmapFont font) {
        TextureRegion toDraw;
        if (isToggled()) {
            toDraw = getFilledTexture();
        } else {
            toDraw = getEmptyTexture();
        }

        float yOffset = 0;
        float xOffset = 0;
        switch (buttonState) {
            case IDLE:
            case HOVER:
                batch.draw(toDraw, position.x, position.y, minSize.x, minSize.y);
                break;
            case ACTIVE:
                yOffset = yActiveOffset;
                xOffset = xActiveOffset;
                batch.draw(toDraw, position.x + xOffset, position.y + yOffset, minSize.x, minSize.y);
                break;
        }
    }
}
