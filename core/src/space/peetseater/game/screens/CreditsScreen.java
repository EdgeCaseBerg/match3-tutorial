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

public class CreditsScreen extends ScreenAdapter implements Scene {
    private final Match3Game match3Game;
    private OrthographicCamera camera;
    private FitViewport viewport;

    public CreditsScreen(Match3Game match3Game) {
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

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.font.draw(match3Game.batch, "Credits", 1, 9);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            match3Game.closeOverlaidScene();
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
        LinkedList<AssetDescriptor<?>> l = new LinkedList<>();
        l.add(Match3Assets.scoreFont);
        return l;
    }
}
