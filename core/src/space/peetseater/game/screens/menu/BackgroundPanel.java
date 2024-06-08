package space.peetseater.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.Match3Assets;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class BackgroundPanel {

    private final Vector2 position;
    private final Vector2 size;
    private final Match3Assets match3Assets;
    private NinePatch bg;

    public BackgroundPanel(Vector2 position, Vector2 size, Match3Assets match3Assets) {
        this.position = position;
        this.size = size;
        this.match3Assets = match3Assets;
        this.bg = null;
    }

    protected void make9Patch() {
        Texture texture = match3Assets.getButton9PatchTexture();
        TextureRegion[][] regions = TextureRegion.split(texture, 32, 32);
        this.bg = new NinePatch(regions[0][0], 8, 8, 8, 8);
        bg.scale(GAME_WIDTH / (float) Gdx.graphics.getWidth(), GAME_HEIGHT /(float) Gdx.graphics.getHeight());
    }

    public void render(float delta, SpriteBatch batch) {
        if (this.bg == null) {
            make9Patch();
        }
        Color batchColor = batch.getColor().cpy();
        batch.setColor(batchColor.r, batchColor.g, batchColor.b, 0.8f);
        bg.draw(batch, position.x, position.y, size.x, size.y);
        batch.setColor(batchColor);
    }
}

