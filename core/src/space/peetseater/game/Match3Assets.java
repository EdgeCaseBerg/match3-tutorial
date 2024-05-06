package space.peetseater.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import static space.peetseater.game.Constants.*;

public class Match3Assets {

    public static final String BACKGROUND_TEXTURE_KEY = "textures/stringstar-fields-example-bg.png";
    public static String SCORE_FONT_KEY = "scorefont.ttf";

    AssetManager assetManager;
    public Match3Assets() {
        assetManager = new AssetManager();
        FileHandleResolver resolver = assetManager.getFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public boolean loadEssentialAssets() {
        queueFont(0.5f, SCORE_FONT_KEY);
        queueBackgroundTexture();
        assetManager.finishLoading();
        return assetManager.update();
    }

    public void queueFont(float ratioToOneTile, String key) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter param = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        param.fontFileName ="font/PoeRedcoatNew-Y5Ro.ttf";
        param.fontParameters.size = (int)((TILE_UNIT_HEIGHT / GAME_HEIGHT) * Gdx.graphics.getHeight() * ratioToOneTile);
        param.fontParameters.color = Color.BLUE;
        param.fontParameters.borderColor = Color.WHITE;
        param.fontParameters.borderWidth = 2;
        AssetDescriptor<BitmapFont> bitmapFontAssetDescriptor = new AssetDescriptor<>(
                key,
                BitmapFont.class,
                param
        );
        assetManager.load(bitmapFontAssetDescriptor);
    }

    public BitmapFont getFont() {
        if (assetManager.isLoaded(SCORE_FONT_KEY)) {
            BitmapFont font = assetManager.get(SCORE_FONT_KEY);
            font.setUseIntegerPositions(false);
            font.getData().setScale(
                    (float) GAME_WIDTH / Gdx.graphics.getWidth(),
                    (float) GAME_HEIGHT / Gdx.graphics.getHeight()
            );
            return font;
        }
        // Fallback to crappy bitmap if needed
        return new BitmapFont();
    }

    public void queueBackgroundTexture() {
        assetManager.load(BACKGROUND_TEXTURE_KEY, Texture.class);
    }

    public Texture getGameScreenBackground() {
        if (assetManager.isLoaded(BACKGROUND_TEXTURE_KEY)) {
            return assetManager.get(BACKGROUND_TEXTURE_KEY, Texture.class);
        }
        return TestTexture.makeTexture(Color.BLACK);
    }
}
