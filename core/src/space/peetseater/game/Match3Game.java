package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.NextTileAlgorithms;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class Match3Game extends ApplicationAdapter {
	SpriteBatch batch;
	private BoardManager boardManager;
	GameGrid<TileType> tokenGrid;
	private BitmapFont font;
	private OrthographicCamera camera;
	private FitViewport viewport;
	private TokenGeneratorAlgorithm<TileType> tokenAlgorithm;
	String algo = "WillNotMatch";
	private DragInputAdapter dragInputAdapter;
	Match3GameState match3GameState;
	ScoreManager scoreManager;
	Match3Assets match3Assets;
	private Texture bgTexture;

	@Override
	public void create () {
		match3Assets = new Match3Assets();
		loadAssets();
		batch = new SpriteBatch();
		Vector2 boardPosition = new Vector2(.1f,.1f);
		Vector2 scorePosition = boardPosition.cpy().add(Constants.BOARD_UNIT_WIDTH + 1f, Constants.BOARD_UNIT_HEIGHT - 3f);
		this.tokenGrid = new GameGrid<>(Constants.TOKENS_PER_ROW,Constants.TOKENS_PER_COLUMN);
		tokenAlgorithm = new NextTileAlgorithms.WillNotMatch(tokenGrid);
		for (GridSpace<TileType> gridSpace : tokenGrid) {
			gridSpace.setValue(tokenAlgorithm.next(gridSpace.getRow(), gridSpace.getColumn()));
		}
		boardManager = new BoardManager(boardPosition, tokenGrid, match3Assets);
		scoreManager = new ScoreManager(scorePosition, boardManager, match3Assets);
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

	public void loadAssets() {
		match3Assets.loadEssentialAssets();
		this.font = match3Assets.getFont();
		this.bgTexture = match3Assets.getGameScreenBackground();
	}

	@Override
	public void render () {
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

		float delta = Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		update(delta);

		ScreenUtils.clear(Color.BLACK);
		batch.begin();
		batch.draw(bgTexture, 0, 0, GAME_WIDTH, GAME_HEIGHT);
		boardManager.render(delta, batch);
		scoreManager.render(delta, batch, font);

		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 3);
		font.draw(batch, algo, 10, 2);
		font.draw(batch,
				dragInputAdapter.dragStart + " " +
				dragInputAdapter.dragEnd + "\n" +
				dragInputAdapter.getIsDragging() +
				" " + dragInputAdapter.getWasDragged(),
				9, 5,
				6f, Align.left, true
		);
		if (dragInputAdapter.isDragging) {
			font.draw(batch,
					boardManager.gameXToColumn(dragInputAdapter.dragEnd.x) + " " + boardManager.gameYToRow(dragInputAdapter.dragEnd.y),
					9, 6,
					6f, Align.left, true
			);
		}

		batch.end();
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
		batch.dispose();
		scoreManager.dispose();
		boardManager.dispose();
		match3Assets.dispose();
	}
}
