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
import space.peetseater.game.Match3Assets;
import space.peetseater.game.Match3Game;
import space.peetseater.game.screens.menu.BackgroundPanel;
import space.peetseater.game.screens.menu.ButtonListener;
import space.peetseater.game.screens.menu.MenuButton;
import space.peetseater.game.screens.menu.MenuInputAdapter;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;

import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.*;

public class HelpScreen extends ScreenAdapter implements Scene, ButtonListener {
    private final Match3Game match3Game;
    private final String instructionText;
    private final MenuButton backButton;
    private final Vector2 textPosition;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private List<AssetDescriptor<?>> assets;
    private LinkedList<TileGraphic> tileGraphics;
    MenuInputAdapter menuInputAdapter;
    BackgroundPanel backgroundPanel;

    public HelpScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();

        textPosition = new Vector2(1, GAME_HEIGHT - 1);
        instructionText = "Drag the tokens and try to make matches of at least 3 tokens in a straight line." +
                "\nThe bacon is a multiplier so try to stack as many as you can before making a match!" +
                "\nAvoid making empty plate matches since it will reduce your score." +
                "\nDo your best to reach the target score within 20 moves to keep playing!";
        backgroundPanel = new BackgroundPanel(textPosition.cpy().sub(-3.5f, 5.75f), new Vector2(11, 5), match3Game.match3Assets);

        tileGraphics = new LinkedList<>();
        Vector2 pos = new Vector2(1, GAME_HEIGHT - 3);
        for (TileType tileType : TileType.values()) {
            TileGraphic tileGraphic = new TileGraphic(
                pos.cpy().sub(0, tileGraphics.size()),
                tileType,
                match3Game.match3Assets
            );
            tileGraphics.add(tileGraphic);
        }

        menuInputAdapter = new MenuInputAdapter(viewport);
        backButton = new MenuButton("Back", new Vector2(1, 1), new Vector2(5, match3Game.font.getLineHeight() + BOARD_UNIT_GUTTER), match3Game.match3Assets);
        backButton.addButtonListener(this);
        menuInputAdapter.addSubscriber(backButton);

        assets = new LinkedList<>();
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
        assets.add(Match3Assets.background);
        assets.add(Match3Assets.tokens);
        assets.add(Match3Assets.button9Patch);
        assets.add(Match3Assets.sparkle);
        assets.add(Match3Assets.confirmSFX);
        assets.add(Match3Assets.cancelSFX);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0,0, GAME_WIDTH, GAME_HEIGHT);
        backgroundPanel.render(delta, match3Game.batch);
        match3Game.font.draw(match3Game.batch, "Instructions", textPosition.x, textPosition.y);

        for (TileGraphic tileGraphic : tileGraphics) {
            tileGraphic.render(delta, match3Game.batch);
            match3Game.font.draw(
                match3Game.batch,
            TileType.scoreFor(tileGraphic.getTileType()) + " points",
            tileGraphic.getMovablePoint().getPosition().x + TILE_UNIT_WIDTH + BOARD_UNIT_GUTTER,
            tileGraphic.getMovablePoint().getPosition().y + TILE_UNIT_HEIGHT - BOARD_UNIT_GUTTER
            );
        }

        backButton.render(delta, match3Game.batch, match3Game.font);

        match3Game.font.draw(
            match3Game.batch,
            instructionText,
            5,
            GAME_HEIGHT - 2,
            GAME_WIDTH - 6,
            Align.left,
            true
        );

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
        return assets;
    }

    @Override
    public void buttonClicked(MenuButton menuButton) {
        if (menuButton == backButton) {
            match3Game.closeOverlaidScene();
        }
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(menuInputAdapter);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
