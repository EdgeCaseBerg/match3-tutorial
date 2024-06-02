package space.peetseater.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.GameDifficulty;
import space.peetseater.game.GameSettings;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.Match3Game;
import space.peetseater.game.screens.menu.*;

import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class ConfigurationScreen extends ScreenAdapter implements Scene, ButtonListener {
    private final Match3Game match3Game;
    private final List<AssetDescriptor<?>> assets;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final MenuButton backButton;
    private final MenuInputAdapter menuInputAdapter;
    private final ToggleButton difficultyToggle;
    private final VolumeControl sfxVolumeControl;
    private final VolumeControl bgmVolumeControl;

    public ConfigurationScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();

        assets = new LinkedList<>();
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
        assets.add(Match3Assets.button9Patch);
        assets.add(Match3Assets.confirmSFX);
        assets.add(Match3Assets.cancelSFX);
        assets.add(Match3Assets.titleFont);
        assets.add(Match3Assets.tokens);
        assets.add(Match3Assets.background);

        menuInputAdapter = new MenuInputAdapter(viewport);

        float menuButtonX = GAME_WIDTH / 2f - 2f;

        Vector2 backButtonPosition = new Vector2(menuButtonX, 2);
        Vector2 size = new Vector2(5f, match3Game.font.getLineHeight() + 0.2f);
        backButton = new MenuButton("Back to Title", backButtonPosition, size, match3Game.match3Assets);
        backButton.addButtonListener(this);

        Vector2 difficultPosition = backButtonPosition.cpy().add(0, GAME_HEIGHT - 6);
        difficultyToggle = new ToggleButton("Difficulty: Normal", "Difficulty: Easy", difficultPosition, size, match3Game.match3Assets);
        difficultyToggle.setToggled(GameSettings.getInstance().getDifficult().equals(GameDifficulty.EASY));
        difficultyToggle.addButtonListener(this);

        bgmVolumeControl = new VolumeControl(
                "BGM Volume",
                GameSettings.getInstance().getBgmVolume(),
                backButtonPosition.cpy().add(0, 3.5f),
                size.cpy(),
                match3Game.match3Assets
        );
        bgmVolumeControl.addButtonListener(this);

        sfxVolumeControl = new VolumeControl(
                "SFX Volume",
                GameSettings.getInstance().getSfxVolume(),
                backButtonPosition.cpy().add(0, 2f),
                size.cpy(),
                match3Game.match3Assets
        );
        sfxVolumeControl.addButtonListener(this);

        menuInputAdapter.addSubscriber(backButton);
        menuInputAdapter.addSubscriber(difficultyToggle);
        menuInputAdapter.addSubscriber(bgmVolumeControl);
        menuInputAdapter.addSubscriber(sfxVolumeControl);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            match3Game.closeOverlaidScene();
        }

        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        Color pre = match3Game.batch.getColor().cpy();
        match3Game.batch.setColor(pre.cpy().set(pre.r, pre.g, pre.b, 0.4f));
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0, 0, GAME_WIDTH, GAME_HEIGHT);
        match3Game.batch.setColor(pre);
        match3Game.match3Assets.getTitleFont().draw(
                match3Game.batch,
                "Configuration",
                0, (float) GAME_HEIGHT - 1,
                GAME_WIDTH, Align.center, false
        );
        backButton.render(delta, match3Game.batch, match3Game.font);
        difficultyToggle.render(delta, match3Game.batch, match3Game.font);
        bgmVolumeControl.render(delta, match3Game.batch, match3Game.font);
        sfxVolumeControl.render(delta, match3Game.batch, match3Game.font);
        match3Game.batch.end();
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

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(menuInputAdapter);
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void buttonClicked(MenuButton menuButton) {
        if (menuButton == sfxVolumeControl) {
            GameSettings.getInstance().setSfxVolume(sfxVolumeControl.getVolume());
        }

        if (menuButton == bgmVolumeControl) {
            GameSettings.getInstance().setBgmVolume(bgmVolumeControl.getVolume());
            match3Game.match3Assets.getBGM().setVolume(bgmVolumeControl.getVolume());
        }

        if (menuButton == difficultyToggle) {
            GameDifficulty changeTo = GameDifficulty.NORMAL;
            if (difficultyToggle.isToggled()) {
                changeTo = GameDifficulty.EASY;
            }
            GameSettings.getInstance().setDifficult(changeTo);
        }

        if (menuButton == backButton) {
            match3Game.closeOverlaidScene();
        }
    }
}
