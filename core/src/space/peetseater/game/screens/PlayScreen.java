package space.peetseater.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.*;
import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.NextTileAlgorithms;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class PlayScreen extends ScreenAdapter {
    private Match3Game match3Game;
    private final BoardManager boardManager;
    GameGrid<TileType> tokenGrid;

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    private TokenGeneratorAlgorithm<TileType> tokenAlgorithm;
    String algo = "WillNotMatch";
    private final DragInputAdapter dragInputAdapter;
    Match3GameState match3GameState;
    ScoreManager scoreManager;
    private final Texture bgTexture;

    public PlayScreen(Match3Game match3Game) {
        this.match3Game = match3Game;
        Vector2 boardPosition = new Vector2(.1f,.1f);
        Vector2 scorePosition = boardPosition.cpy().add(Constants.BOARD_UNIT_WIDTH + 1f, Constants.BOARD_UNIT_HEIGHT - 3f);
        this.bgTexture = match3Game.match3Assets.getGameScreenBackground();

        this.tokenGrid = new GameGrid<>(Constants.TOKENS_PER_ROW,Constants.TOKENS_PER_COLUMN);
        tokenAlgorithm = new NextTileAlgorithms.WillNotMatch(tokenGrid);
        for (GridSpace<TileType> gridSpace : tokenGrid) {
            gridSpace.setValue(tokenAlgorithm.next(gridSpace.getRow(), gridSpace.getColumn()));
        }
        boardManager = new BoardManager(boardPosition, tokenGrid, match3Game.match3Assets);
        scoreManager = new ScoreManager(scorePosition, boardManager, match3Game.match3Assets);
        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false);
        camera.update();
        this.match3GameState = new Match3GameState(boardManager, tokenGrid, tokenAlgorithm);
        this.match3GameState.addSubscriber(scoreManager);
        this.dragInputAdapter = new DragInputAdapter(viewport);
        this.dragInputAdapter.addSubscriber(match3GameState);
        Gdx.input.setInputProcessor(dragInputAdapter);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            tokenAlgorithm = new NextTileAlgorithms.WillNotMatch(tokenGrid);
            algo = "WillNotMatch";
            match3GameState.setTokenAlgorithm(tokenAlgorithm);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            tokenAlgorithm = new NextTileAlgorithms.LikelyToMatch(tokenGrid);
            algo = "LikelyToMatch";
            match3GameState.setTokenAlgorithm(tokenAlgorithm);
        }

        camera.update();
        match3Game.batch.setProjectionMatrix(camera.combined);
        update(delta);

        ScreenUtils.clear(Color.BLACK);
        match3Game.batch.begin();
        match3Game.batch.draw(bgTexture, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        boardManager.render(delta, match3Game.batch);
        scoreManager.render(delta, match3Game.batch, match3Game.font);

        match3Game.font.draw(match3Game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 3);
        match3Game.font.draw(match3Game.batch, algo, 10, 2);
        match3Game.font.draw(match3Game.batch,
                dragInputAdapter.dragStart + " " +
                        dragInputAdapter.dragEnd + "\n" +
                        dragInputAdapter.getIsDragging() +
                        " " + dragInputAdapter.getWasDragged(),
                9, 5,
                6f, Align.left, true
        );
        if (dragInputAdapter.isDragging) {
            match3Game.font.draw(match3Game.batch,
                    boardManager.gameXToColumn(dragInputAdapter.dragEnd.x) + " " + boardManager.gameYToRow(dragInputAdapter.dragEnd.y),
                    9, 6,
                    6f, Align.left, true
            );
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
        scoreManager.dispose();
        boardManager.dispose();
    }
}
