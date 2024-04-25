package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.grid.match.MatchSubscriber;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.NextTileAlgorithms;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.List;

public class Match3Game extends ApplicationAdapter implements MatchSubscriber<TileType> {
	SpriteBatch batch;
	private BoardGraphic boardGraphic;
	GameGrid<TileType> tokenGrid;
	private BitmapFont font;
	static public int GAME_WIDTH = 16;
	static public int GAME_HEIGHT = 10;
	private OrthographicCamera camera;
	private FitViewport viewport;
	private TokenGeneratorAlgorithm<TileType> tokenAlgorithm;
	String algo = "WillNotMatch";
	private DragInputAdapter dragInputAdapter;

	Match3GameState match3GameState;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Vector2 boardPosition = new Vector2(.1f,.1f);
		this.tokenGrid = new GameGrid<>(Constants.TOKENS_PER_ROW,Constants.TOKENS_PER_COLUMN);
		tokenAlgorithm = new NextTileAlgorithms.WillNotMatch(tokenGrid);
		for (GridSpace<TileType> gridSpace : tokenGrid) {
			gridSpace.setValue(tokenAlgorithm.next(gridSpace.getRow(), gridSpace.getColumn()));
		}
		boardGraphic = new BoardGraphic(boardPosition, tokenGrid);
		font = new BitmapFont();
		camera = new OrthographicCamera();
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		camera.setToOrtho(false);
		camera.update();
		this.match3GameState = new Match3GameState(boardGraphic, tokenGrid, tokenAlgorithm);
		this.match3GameState.addSubscriber(this);
		this.dragInputAdapter = new DragInputAdapter(viewport);
		this.dragInputAdapter.addSubscriber(match3GameState);
		Gdx.input.setInputProcessor(dragInputAdapter);
	}

	@Override
	public void render () {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			tokenAlgorithm = new NextTileAlgorithms.WillNotMatch(tokenGrid);
			algo = "WillNotMatch";
			for (GridSpace<TileType> space : tokenGrid) {
				TileType next = tokenAlgorithm.next(space.getRow(), space.getColumn());
				space.setValue(next);
				boardGraphic.replaceTile(space.getRow(), space.getColumn(), next);
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			tokenAlgorithm = new NextTileAlgorithms.LikelyToMatch(tokenGrid);
			for (GridSpace<TileType> space : tokenGrid) {
				TileType next = tokenAlgorithm.next(space.getRow(), space.getColumn());
				space.setValue(next);
				boardGraphic.replaceTile(space.getRow(), space.getColumn(), next);
			}
			algo = "LikelyToMatch";
		}

		float delta = Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		font.setUseIntegerPositions(false);
		font.getData().setScale(16f / Gdx.graphics.getWidth(), 16f / Gdx.graphics.getHeight());

		update(delta);

		ScreenUtils.clear(Color.BLACK);
		batch.begin();
		boardGraphic.render(delta, batch);

		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 14, 7);
		font.draw(batch, algo, 14, 6);
		font.draw(batch, dragInputAdapter.dragStart + " " + dragInputAdapter.dragEnd + " " + dragInputAdapter.getIsDragging() + " " + dragInputAdapter.getWasDragged(), 9, 5);
		if (dragInputAdapter.isDragging) {
			font.draw(batch, boardGraphic.gameXToColumn(dragInputAdapter.dragEnd.x) + " " + boardGraphic.gameYToRow(dragInputAdapter.dragEnd.y), 9, 4);
		}

		batch.end();
	}

	public void update(float delta) {
		match3GameState.update(delta);
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
	}

	@Override
	public void onMatches(List<Match<TileType>> matches) {
		// TODO: show this score to the user and whatnot
		Gdx.app.log("On Match from Game", matches.toString());
	}
}
