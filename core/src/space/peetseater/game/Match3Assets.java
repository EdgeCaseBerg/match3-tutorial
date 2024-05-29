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
import space.peetseater.game.screens.Scene;
import space.peetseater.game.tile.TileType;

import static space.peetseater.game.Constants.*;

public class Match3Assets implements Disposable {

    public static final String BACKGROUND_TEXTURE_KEY = "textures/stringstar-fields-example-bg.png";
    public static final AssetDescriptor<Texture> background = new AssetDescriptor<>(BACKGROUND_TEXTURE_KEY, Texture.class);

    public static String SCORE_FONT_KEY = "scorefont.ttf";
    public static final AssetDescriptor<BitmapFont> scoreFont = fontDescriptorForSize(0.5f, SCORE_FONT_KEY);

    public static String TITLE_FONT_KEY = "titlefont.ttf";
    public static final AssetDescriptor<BitmapFont> titleFont = fontDescriptorForSize(1.5f, TITLE_FONT_KEY);

    public static String BUTTON_NINE_PATCH_KEY = "textures/button9patch.png";
    public static final AssetDescriptor<Texture> button9Patch = new AssetDescriptor<>(BUTTON_NINE_PATCH_KEY, Texture.class);

    public static final String TOKEN_SPRITE_SHEET_KEY = "textures/tokens/tokens.png";
    public static final AssetDescriptor<Texture> tokens = new AssetDescriptor<>(TOKEN_SPRITE_SHEET_KEY, Texture.class);

    public static final int TOKEN_SPRITE_PIXEL_WIDTH = 32;
    public static final int TOKEN_SPRITE_PIXEL_HEIGHT = 32;
    public static final int TOKEN_SPRITE_IDLE_START = 0;
    public static final int TOKEN_SPRITE_SELECTED_START = 32;

    public static final String SPARKLE_SHEET_KEY = "textures/sparkle.png";
    public static final AssetDescriptor<Texture> sparkle = new AssetDescriptor<>(SPARKLE_SHEET_KEY, Texture.class);

    public static final int SPARKLE_SPRITE_WIDTH = 9;
    public static final int SPARKLE_SPRITE_HEIGHT = 9;

    public static final String SCORE_SFX_KEY = "sound/8-bit-16-bit-sound-effects-pack/Big Egg collect 1.mp3";
    public static final AssetDescriptor<Sound> scoreSFX = new AssetDescriptor<>(SCORE_SFX_KEY, Sound.class);

    public static final String MULTIPLIER_SFX_KEY = "sound/--Pixelated UI/Pixel_08.wav";
    public static final AssetDescriptor<Sound> multiplierSFX = new AssetDescriptor<>(MULTIPLIER_SFX_KEY, Sound.class);

    public static final String SELECT_SFX_KEY = "sound/8-bit-16-bit-sound-effects-pack/Bubble 1.mp3";
    public static final AssetDescriptor<Sound> selectSFX = new AssetDescriptor<>(SELECT_SFX_KEY, Sound.class);

    public static final String NEGATIVE_SFX_KEY = "sound/--Pixelated UI/Pixel_11.wav";
    public static final AssetDescriptor<Sound> negativeSFX = new AssetDescriptor<>(NEGATIVE_SFX_KEY, Sound.class);

    public static final String CONFIRM_SFX_KEY = "sound/8-bit-16-bit-sound-effects-pack/Confirm 1.mp3";
    public static final AssetDescriptor<Sound> confirmSFX = new AssetDescriptor<>(CONFIRM_SFX_KEY, Sound.class);

    public static final String CANCEL_SFX_KEY = "sound/8-bit-16-bit-sound-effects-pack/Cancel 1.mp3";
    public static final AssetDescriptor<Sound> cancelSFX = new AssetDescriptor<>(CANCEL_SFX_KEY, Sound.class);

    public static final String BGM_KEY = "sound/ogg-short-loopable-background-music/Lost in the Dessert.ogg";
    public static final AssetDescriptor<Music> bgm = new AssetDescriptor<>(BGM_KEY, Music.class);

    public AssetManager assetManager;

    public Match3Assets() {
        assetManager = new AssetManager();
        Texture.setAssetManager(assetManager);
        FileHandleResolver resolver = assetManager.getFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    public boolean queueAssets() {
        queueFont();
        queueBackgroundTexture();
        queueTokenSheetTexture();
        queueSparkleSheetTexture();
        queueSounds();
        queueBGM();
        return assetManager.update();
    }

    static public AssetDescriptor<BitmapFont> fontDescriptorForSize(float ratioToOneTile, String key) {
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
        return bitmapFontAssetDescriptor;
    }

    public void queueFont(float ratioToOneTile, String key) {
        AssetDescriptor<BitmapFont> bitmapFontAssetDescriptor = fontDescriptorForSize(ratioToOneTile, key);
        assetManager.load(bitmapFontAssetDescriptor);
    }

    public void queueFont() {
        queueFont(0.5f, SCORE_FONT_KEY);
    }

    public BitmapFont getFont() {
        if (assetManager.isLoaded(scoreFont)) {
            BitmapFont font = assetManager.get(scoreFont);
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
        assetManager.load(background);
    }

    public Texture getGameScreenBackground() {
        return assetManager.get(background);
    }

    public void queueTokenSheetTexture() {
        assetManager.load(tokens);
    }

    public Texture getTokenSheetTexture() {
        return assetManager.get(tokens);
    }

    public void unloadTokenSheetTexture() {
        assetManager.unload(tokens.fileName);
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

    public Sound getMultiplierSFX() {
        return assetManager.get(multiplierSFX);
    }

    public void unloadMultiplierSFX() {
        assetManager.unload(multiplierSFX.fileName);
    }

    public Sound getIncrementScoreSFX() {
        return assetManager.get(scoreSFX);
    }

    public void unloadIncrementScoreSFX() {
        assetManager.unload(scoreSFX.fileName);
    }

    public Sound getSelectSFX() {
        return assetManager.get(selectSFX);
    }

    public void unloadSelectSFX() {
        assetManager.unload(selectSFX.fileName);
    }

    public Sound getNegativeSFX() {
        return assetManager.get(negativeSFX);
    }

    public void unloadNegativeSFX() {
        assetManager.unload(negativeSFX.fileName);
    }

    public void queueBGM() {
        assetManager.load(bgm);
    }

    public Music getBGM() {
        return assetManager.get(bgm);
    }

    public void unloadBGM() {
        assetManager.unload(bgm.fileName);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public void blockingLoad() {
        assetManager.finishLoading();
    }

    public int getProgress() {
        float zeroToOne = assetManager.getProgress();
        return (int) (zeroToOne * 100);
    };

    public void queue(Scene scene) {
        for (AssetDescriptor<?> asset : scene.getRequiredAssets()) {
            assetManager.load(asset);
        }
    }

    public void unloadFont() {
        assetManager.unload(scoreFont.fileName);
    }

    public void unload(AssetDescriptor<?> asset) {
        assetManager.unload(asset.fileName);
    }

    public Texture getButton9PatchTexture() {
        return assetManager.get(button9Patch);
    }

    public BitmapFont getTitleFont() {
        BitmapFont font = assetManager.get(titleFont);
        font.setUseIntegerPositions(false);
        font.getData().setScale(
                (float) GAME_WIDTH / Gdx.graphics.getWidth(),
                (float) GAME_HEIGHT / Gdx.graphics.getHeight()
        );
        return font;
    }

    public Sound getConfirmSFX() {
        return assetManager.get(confirmSFX);
    }

    public Sound getCancelSFX() {
        return assetManager.get(cancelSFX);
    }
}
