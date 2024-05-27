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

public class LoadingScreen extends ScreenAdapter implements Scene {
    private final Match3Game match3Game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    public boolean hasConfirmedLoading = false;


    public LoadingScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);
        int progress = match3Game.match3Assets.getProgress();

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.font.draw(
                match3Game.batch,
                "Loading: " + progress + "%",
                6, 6
        );
        if (!hasConfirmedLoading && progress == 100) {
            match3Game.font.draw(
                    match3Game.batch,
                    "Click to continue",
                    6, 5
            );
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                match3Game.replaceSceneWith(new LoadingScreen(match3Game));
            }
            if (Gdx.input.isTouched()) {
                hasConfirmedLoading = true;
            }
        }
        match3Game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }


    public boolean isLoadingComplete() {
        return hasConfirmedLoading;
    }

    public void setHasConfirmedLoading(boolean hasConfirmedLoading) {
        this.hasConfirmedLoading = hasConfirmedLoading;
    }

    @Override
    public List<AssetDescriptor<?>> getRequiredAssets() {
        LinkedList<AssetDescriptor<?>> l = new LinkedList<>();
        l.add(Match3Assets.scoreFont);
        return l;
    }
}
