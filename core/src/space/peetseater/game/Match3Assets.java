package space.peetseater.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Disposable;
import space.peetseater.game.shared.EmptySound;
import space.peetseater.game.tile.TileType;

import static space.peetseater.game.Constants.*;

public class Match3Assets implements Disposable {

    public static final String BACKGROUND_TEXTURE_KEY = "textures/stringstar-fields-example-bg.png";

    public static String SCORE_FONT_KEY = "scorefont.ttf";

    public static final String TOKEN_SPRITE_SHEET_KEY = "textures/tokens/tokens.png";

    public static final int TOKEN_SPRITE_PIXEL_WIDTH = 32;
    public static final int TOKEN_SPRITE_PIXEL_HEIGHT = 32;
    public static final int TOKEN_SPRITE_IDLE_START = 0;
    public static final int TOKEN_SPRITE_SELECTED_START = 32;

    public static final String SPARKLE_SHEET_KEY = "textures/sparkle.png";

    public static final int SPARKLE_SPRITE_WIDTH = 9;
    public static final int SPARKLE_SPRITE_HEIGHT = 9;


    public static final String SCORE_SFX_KEY = "sound/8-bit-16-bit-sound-effects-pack/Big Egg collect 1.mp3";
    public static final String MULTIPLIER_SFX_KEY = "sound/--Pixelated UI/Pixel_08.wav";
    public static final String SELECT_SFX_KEY = "sound/8-bit-16-bit-sound-effects-pack/Bubble 1.mp3";
    public static final String NEGATIVE_SFX_KEY = "sound/--Pixelated UI/Pixel_11.wav";

    public static final String BGM_KEY = "sound/ogg-short-loopable-background-music/Lost in the Dessert.ogg";

    AssetManager assetManager;
    private final Sound emptySound = new EmptySound();

    public Match3Assets() {
        assetManager = new AssetManager();
        FileHandleResolver resolver = assetManager.getFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public boolean loadEssentialAssets() {
        queueFont(0.5f, SCORE_FONT_KEY);
        queueBackgroundTexture();
        queueTokenSheetTexture();
        queueSparkleSheetTexture();
        queueSounds();
        queueBGM();
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

    public void queueTokenSheetTexture() {
        assetManager.load(TOKEN_SPRITE_SHEET_KEY, Texture.class);
    }

    public Texture getTokenSheetTexture() {
        return assetManager.get(TOKEN_SPRITE_SHEET_KEY, Texture.class);
    }

    public void unloadTokenSheetTexture() {
        assetManager.unload(TOKEN_SPRITE_SHEET_KEY);
    }

    public int getStartYOfTokenInSheet(TileType tileType) {
        switch (tileType) {
            case HighValue:
                return 0 * TOKEN_SPRITE_PIXEL_HEIGHT;
            case MidValue:
                return 1 * TOKEN_SPRITE_PIXEL_HEIGHT;
            case LowValue:
                return 2 * TOKEN_SPRITE_PIXEL_HEIGHT;
            case Multiplier:
                return 3 * TOKEN_SPRITE_PIXEL_HEIGHT;
            case Negative:
                return 4 * TOKEN_SPRITE_PIXEL_HEIGHT;
        }
        return 0;
    }

    public void queueSparkleSheetTexture() {
        assetManager.load(SPARKLE_SHEET_KEY, Texture.class);
    }

    public Texture getSparkleSheetTexture() {
        return assetManager.get(SPARKLE_SHEET_KEY, Texture.class);
    }

    public void unloadSparkleSheetTexture() {
        assetManager.unload(SPARKLE_SHEET_KEY);
    }

    public void queueSounds() {
        assetManager.load(SCORE_SFX_KEY, Sound.class);
        assetManager.load(MULTIPLIER_SFX_KEY, Sound.class);
        assetManager.load(SELECT_SFX_KEY, Sound.class);
        assetManager.load(NEGATIVE_SFX_KEY, Sound.class);
    }

    private Sound getSFXOrEmpty(String key) {
        if (assetManager.isLoaded(key)) {
            return assetManager.get(key, Sound.class);
        }
        return emptySound;
    }

    public Sound getMultiplierSFX() {
        return getSFXOrEmpty(MULTIPLIER_SFX_KEY);
    }

    public void unloadMultiplierSFX() {
        assetManager.unload(MULTIPLIER_SFX_KEY);
    }

    public Sound getIncrementScoreSFX() {
        return getSFXOrEmpty(SCORE_SFX_KEY);
    }

    public void unloadIncrementScoreSFX() {
        assetManager.unload(SCORE_SFX_KEY);
    }

    public Sound getSelectSFX() {
        return getSFXOrEmpty(SELECT_SFX_KEY);
    }

    public void unloadSelectSFX() {
        assetManager.unload(SELECT_SFX_KEY);
    }

    public Sound getNegativeSFX() {
        return getSFXOrEmpty(NEGATIVE_SFX_KEY);
    }

    public void unloadNegativeSFX() {
        assetManager.unload(NEGATIVE_SFX_KEY);
    }

    public void queueBGM() {
        assetManager.load(BGM_KEY, Music.class);
    }

    public Music getBGM() {
        return assetManager.get(BGM_KEY, Music.class);
    }

    public void unloadBGM() {
        assetManager.unload(BGM_KEY);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
