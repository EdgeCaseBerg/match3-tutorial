package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;

public class Match3Game extends ApplicationAdapter {
	SpriteBatch batch;
	private BoardGraphic boardGraphic;
	GameGrid<TileType> tokenGrid;
	private BitmapFont font;
	static public int GAME_WIDTH = 16;
	static public int GAME_HEIGHT = 10;


	private OrthographicCamera camera;
	private FitViewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Vector2 boardPosition = new Vector2(.1f,.1f);
		this.tokenGrid = new GameGrid<>(Constants.TOKENS_PER_ROW,Constants.TOKENS_PER_COLUMN);
		for (GridSpace<TileType> gridSpace : tokenGrid) {
			gridSpace.setValue(TileType.values()[(MathUtils.random(TileType.values().length - 1))]);
		}
		boardGraphic = new BoardGraphic(boardPosition, tokenGrid);
		font = new BitmapFont();
		camera = new OrthographicCamera();
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		camera.setToOrtho(false);
		camera.update();
	}

	@Override
	public void render () {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		float delta = Gdx.graphics.getDeltaTime();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		font.setUseIntegerPositions(false);
		font.getData().setScale(16f / Gdx.graphics.getWidth(), 16f / Gdx.graphics.getHeight());
		ScreenUtils.clear(Color.BLACK);
		batch.begin();
		boardGraphic.render(delta, batch);

		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 14, 7);
		batch.end();
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
}
