package space.peetseater.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.Match3Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class CreditsScreen extends ScreenAdapter implements Scene {
    private final Match3Game match3Game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    List<String> credits;
    float timeForEachCredit = 3f;
    float accum;
    int idxToShow = 0;
    private List<AssetDescriptor<?>> assets;

    public CreditsScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();
        credits = new ArrayList<>();

        assets = new LinkedList<>();
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
        assets.add(Match3Assets.background);

        credits.add("Graphics");
        credits.add("");
        credits.add("Token Graphics");
        credits.add("https://ghostpixxells.itch.io/pixelfood");
        credits.add("");
        credits.add("Background images");
        credits.add("https://trixelized.itch.io/starstring-fields");
        credits.add("");
        credits.add("Button Graphics and sparkles");
        credits.add("Created in piskel by peetseater");
        credits.add("");
        credits.add("Poe Redcoat New Font");
        credits.add("https://www.fontspace.com/poe-redcoat-new-font-f23843");
        credits.add("");
        credits.add("Music");
        credits.add("");
        credits.add("Background music");
        credits.add("https://joshuuu.itch.io/short-loopable-background-music");
        credits.add("");
        credits.add("Sound Effects");
        credits.add("https://jdwasabi.itch.io/8-bit-16-bit-sound-effects-pack");
        credits.add("https://ateliermagicae.itch.io/pixel-ui-sound-effects");
        credits.add("");
        credits.add("Programming");
        credits.add("Made in LibGDX by peetseater");
        credits.add("");
        credits.add("Thank you for playing!");
        credits.add("");
        credits.add("Press ESC to return to the title screen");
        credits.add("");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0,0, GAME_WIDTH, GAME_HEIGHT);
        match3Game.font.draw(match3Game.batch, "Credits", 1, GAME_HEIGHT - 1);
        float alpha = 1 - MathUtils.lerp(0, 1, accum / timeForEachCredit);

        String credit = getCreditToShow(delta);
        Color restoreTo = match3Game.font.getColor().cpy();
        match3Game.font.setColor(restoreTo.cpy().set(restoreTo.r, restoreTo.g, restoreTo.b, alpha));
        match3Game.font.draw(match3Game.batch, credit, 2, (float) GAME_HEIGHT / 2, GAME_WIDTH - 2, Align.left, true);
        match3Game.font.setColor(restoreTo);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            match3Game.closeOverlaidScene();
        }
        match3Game.batch.end();
    }

    private String getCreditToShow(float delta) {
        accum += delta;
        if (accum > timeForEachCredit) {
            accum = 0;
            idxToShow++;
        }
        return credits.get(idxToShow % credits.size());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public List<AssetDescriptor<?>> getRequiredAssets() {
        return assets;
    }
}
