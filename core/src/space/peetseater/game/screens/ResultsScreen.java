package space.peetseater.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.GameStats;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.Match3Game;
import space.peetseater.game.screens.menu.ButtonListener;
import space.peetseater.game.screens.menu.MenuButton;
import space.peetseater.game.screens.menu.MenuInputAdapter;

import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class ResultsScreen extends ScreenAdapter implements Scene, ButtonListener {

    private final Match3Game match3Game;
    private final GameStats gameStats;
    private final MenuButton titleScreenButton;
    private final MenuButton restartButton;
    private final MenuInputAdapter menuInputAdapter;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private List<AssetDescriptor<?>> assets;

    public ResultsScreen(Match3Game match3Game, GameStats gameStats) {
        this.match3Game = match3Game;
        this.gameStats = gameStats;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();
        assets = new LinkedList<>();
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
        assets.add(Match3Assets.background);
        assets.add(Match3Assets.button9Patch);
        Vector2 position = new Vector2((float) GAME_HEIGHT / 2, 3);
        Vector2 size = new Vector2(5f, match3Game.font.getLineHeight() + 0.2f);
        this.titleScreenButton = new MenuButton("Title Screen" , position, size, match3Game.match3Assets);
        this.titleScreenButton.addButtonListener(this);

        this.restartButton = new MenuButton("Restart" , position.cpy().sub(0, 1), size, match3Game.match3Assets);
        this.restartButton.addButtonListener(this);

        menuInputAdapter = new MenuInputAdapter(viewport);
        menuInputAdapter.addSubscriber(titleScreenButton, restartButton);
        Gdx.input.setInputProcessor(menuInputAdapter);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0,0, GAME_WIDTH, GAME_HEIGHT);
        match3Game.font.draw(match3Game.batch, "Results", 1, GAME_HEIGHT - 1);
        match3Game.font.draw(match3Game.batch, "Total Moves " + gameStats.getMoves(), 1, GAME_HEIGHT - 2);
        match3Game.font.draw(match3Game.batch, "Total Score " + gameStats.getScore(), 1, GAME_HEIGHT - 3);
        match3Game.font.draw(match3Game.batch, "Good job! Hope you relaxed!", 1, GAME_HEIGHT - 4);
        titleScreenButton.render(delta, match3Game.batch, match3Game.font);
        restartButton.render(delta, match3Game.batch, match3Game.font);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            match3Game.replaceSceneWith(new TitleScreen(match3Game));
        }
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
    public void buttonClicked(MenuButton menuButton) {
        if (menuButton == this.restartButton) {
            match3Game.replaceSceneWith(new PlayScreen(match3Game));
        }
        if(menuButton == this.titleScreenButton) {
            match3Game.replaceSceneWith(new TitleScreen(match3Game));
        }
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(menuInputAdapter);
    }
}
