package space.peetseater.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.Match3Assets;
import space.peetseater.game.Match3Game;

import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class ConfigurationScreen extends ScreenAdapter implements Scene {
    private final Match3Game match3Game;
    private final List<AssetDescriptor<?>> assets;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public ConfigurationScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();
        assets = new LinkedList<>();
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
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
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0, 0);
        match3Game.font.draw(
                match3Game.batch,
                "Configuration",
                (float) GAME_WIDTH / 2 - 2, (float) GAME_HEIGHT / 2 + 2
        );
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
}
