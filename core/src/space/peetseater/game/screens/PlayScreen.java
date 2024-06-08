package space.peetseater.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.*;
import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.screens.menu.ButtonListener;
import space.peetseater.game.screens.menu.MenuButton;
import space.peetseater.game.screens.menu.MenuInputAdapter;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.NextTileAlgorithms;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.LinkedList;
import java.util.List;

import static space.peetseater.game.Constants.*;

public class PlayScreen extends ScreenAdapter implements Scene, EndGameSubscriber, ButtonListener {
    private final MenuButton settingsButton;
    private Match3Game match3Game;
    private final BoardManager boardManager;
    GameGrid<TileType> tokenGrid;

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    private TokenGeneratorAlgorithm<TileType> tokenAlgorithm;
    private final DragInputAdapter dragInputAdapter;
    private final MenuInputAdapter menuInputAdapter;
    Match3GameState match3GameState;
    ScoreManager scoreManager;
    private List<AssetDescriptor<?>> assets;
    private MenuButton quitButton;
    private final MenuButton helpButton;

    public PlayScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        Vector2 boardPosition = new Vector2(.1f,.1f);
        Vector2 scorePosition = boardPosition.cpy().add(Constants.BOARD_UNIT_WIDTH + 1f, Constants.BOARD_UNIT_HEIGHT - 1.5f);

        this.tokenGrid = new GameGrid<>(Constants.TOKENS_PER_ROW,Constants.TOKENS_PER_COLUMN);
        tokenAlgorithm = new NextTileAlgorithms.WillNotMatch(tokenGrid);
        for (GridSpace<TileType> gridSpace : tokenGrid) {
            gridSpace.setValue(tokenAlgorithm.next(gridSpace.getRow(), gridSpace.getColumn()));
        }
        if (GameSettings.getInstance().getDifficult().equals(GameDifficulty.EASY)) {
            this.tokenAlgorithm = new NextTileAlgorithms.LikelyToMatch(tokenGrid);
        }
        boardManager = new BoardManager(boardPosition, tokenGrid, match3Game.match3Assets);
        scoreManager = new ScoreManager(scorePosition, boardManager, match3Game.match3Assets);
        scoreManager.addEndGameSubscriber(this);

        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false);
        camera.update();

        menuInputAdapter = new MenuInputAdapter(viewport);
        Vector2 buttonMinSize = new Vector2(5f, match3Game.font.getLineHeight() + 0.2f);

        helpButton = new MenuButton("How to play", scorePosition.cpy().sub(0, 5), buttonMinSize, match3Game.match3Assets);
        helpButton.addButtonListener(this);
        menuInputAdapter.addSubscriber(helpButton);

        settingsButton = new MenuButton("Settings", scorePosition.cpy().sub(0, 6), buttonMinSize, match3Game.match3Assets);
        settingsButton.addButtonListener(this);
        menuInputAdapter.addSubscriber(settingsButton);


        quitButton = new MenuButton("Quit to title", scorePosition.cpy().sub(0, 7), buttonMinSize, match3Game.match3Assets);
        quitButton.addButtonListener(this);
        menuInputAdapter.addSubscriber(quitButton);


        this.match3GameState = new Match3GameState(boardManager, tokenGrid, tokenAlgorithm);
        this.match3GameState.addSubscriber(scoreManager);
        this.match3GameState.addMoveSubscriber(scoreManager);
        this.dragInputAdapter = new DragInputAdapter(viewport);
        this.dragInputAdapter.addSubscriber(match3GameState);

        assets = new LinkedList<>();
        assets.add(Match3Assets.background);
        assets.add(Match3Assets.multiplierSFX);
        assets.add(Match3Assets.scoreSFX);
        assets.add(Match3Assets.selectSFX);
        assets.add(Match3Assets.negativeSFX);
        assets.add(Match3Assets.sparkle);
        assets.add(Match3Assets.tokens);
        assets.add(Match3Assets.scoreFont);
        assets.add(Match3Assets.bgm);
        assets.add(Match3Assets.button9Patch);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            match3Game.replaceSceneWith(new TitleScreen(match3Game));
        }

        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);
        update(delta);

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.batch.draw(match3Game.match3Assets.getGameScreenBackground(), 0, 0, GAME_WIDTH, GAME_HEIGHT);
        boardManager.render(delta, match3Game.batch);
        scoreManager.render(delta, match3Game.batch, match3Game.font);
        helpButton.render(delta, match3Game.batch, match3Game.font);
        quitButton.render(delta, match3Game.batch, match3Game.font);
        settingsButton.render(delta, match3Game.batch, match3Game.font);

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            match3Game.font.draw(match3Game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 3);
        }

        match3Game.batch.end();
    }

    public void update(float delta) {
        match3GameState.update(delta);
        scoreManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
        viewport.apply(true);
    }

    @Override
    public void dispose () {
        for (AssetDescriptor<?> asset : assets) {
            match3Game.match3Assets.unload(asset);
        }
        match3GameState.removeMoveSubscriber(scoreManager);
        scoreManager.removeEndGameSubscriber(this);
        scoreManager.dispose();
    }

    @Override
    public List<AssetDescriptor<?>> getRequiredAssets() {
        return assets;
    }

    @Override
    public void show() {
        super.show();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(menuInputAdapter);
        inputMultiplexer.addProcessor(dragInputAdapter);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void notifyGameShouldEnd(GameStats gameStats) {
        match3Game.replaceSceneWith(new ResultsScreen(match3Game, gameStats));
    }

    @Override
    public void buttonClicked(MenuButton menuButton) {
        if (menuButton == quitButton) {
            match3Game.replaceSceneWith(new TitleScreen(match3Game));
        }
        if (menuButton == settingsButton) {
            match3Game.overlayScene(new ConfigurationScreen(match3Game));
        }
        if (menuButton == helpButton) {
            // TODO! Overlay our new help screen here!
        }
    }
}
