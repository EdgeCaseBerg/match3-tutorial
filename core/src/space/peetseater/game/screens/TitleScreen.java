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
import space.peetseater.game.screens.menu.ButtonListener;
import space.peetseater.game.screens.menu.MenuButton;
import space.peetseater.game.screens.menu.MenuInputAdapter;

import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class TitleScreen extends ScreenAdapter implements Scene, ButtonListener {
    public static final String START_GAME = "Start Game";
    public static final String SETTINGS = "Settings";
    public static final String CREDITS = "Credits";
    public static final String QUIT = "Quit";
    private final Match3Game match3Game;
    private final List<AssetDescriptor<?>> assets;
    private final LinkedList<MenuButton> menuButtons;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MenuInputAdapter menuInputAdapter;

    public TitleScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();
        assets = new LinkedList<>();
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
        assets.add(Match3Assets.background);
        assets.add(Match3Assets.button9Patch);
        assets.add(Match3Assets.titleFont);
        assets.add(Match3Assets.confirmSFX);
        assets.add(Match3Assets.cancelSFX);

        menuButtons = new LinkedList<>();
        Vector2 buttonPositionsStart = new Vector2(GAME_WIDTH / 2f - 2.5f, GAME_HEIGHT / 2f - 1);
        Vector2 buttonMinSize = new Vector2(5f, match3Game.font.getLineHeight() + 0.2f);

        menuInputAdapter = new MenuInputAdapter(viewport);
        Gdx.input.setInputProcessor(menuInputAdapter);

        String[] buttonTexts = new String[]{START_GAME, SETTINGS, CREDITS, QUIT};
        for (int i = 0; i < buttonTexts.length; i++) {
            Vector2 buttonPosition = buttonPositionsStart.cpy().sub(0, i  * buttonMinSize.y);
            MenuButton menuButton = new MenuButton(buttonTexts[i], buttonPosition, buttonMinSize, match3Game.match3Assets);
            menuButton.addButtonListener(this);
            menuButtons.add(menuButton);
            menuInputAdapter.addSubscriber(menuButton);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0, 0, GAME_WIDTH, GAME_HEIGHT);
        match3Game.match3Assets.getTitleFont().draw(
                match3Game.batch,
                "Relax n Match",
                0, GAME_HEIGHT - 2,
                GAME_WIDTH, Align.center, false
        );

        for (MenuButton menuButton : menuButtons) {
            menuButton.render(delta, match3Game.batch, match3Game.font);
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
    public void buttonClicked(MenuButton button) {
        for (MenuButton menuButton : menuButtons) {
            if (button == menuButton) {
                switch (button.getText()) {
                    case START_GAME:
                        match3Game.replaceSceneWith(new PlayScreen(match3Game));
                        break;
                    case SETTINGS:
                        match3Game.overlayScene(new ConfigurationScreen(match3Game));
                        break;
                    case CREDITS:
                        match3Game.overlayScene(new CreditsScreen(match3Game));
                        break;
                    case QUIT:
                        Gdx.app.exit();
                    default:
                        // Do nothing.
                }
            }
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
