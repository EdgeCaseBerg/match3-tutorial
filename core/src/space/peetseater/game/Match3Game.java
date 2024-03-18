package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.shared.commands.MoveTowards;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.tile.commands.DeselectTile;
import space.peetseater.game.tile.commands.IncludeInMatch;
import space.peetseater.game.tile.commands.SelectTile;

import java.util.LinkedList;
import java.util.List;

public class Match3Game extends ApplicationAdapter {
	SpriteBatch batch;
	private List<TileGraphic> tileGraphics;

	String lastKeyPressed = "";
	private BitmapFont font;

	static public int GAME_WIDTH = 16;
	static public int GAME_HEIGHT = 10;


	private OrthographicCamera camera;
	private FitViewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		tileGraphics = new LinkedList<>();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		camera.setToOrtho(false);
		camera.update();

		// Debug, render a 7 by 8 grid
		int numT = 8;
		float gutter = 0.20f;
		for (int c = 0; c < numT - 1; c++) {
			for (int y = 0; y < numT; y++) {
				TileType tileType = TileType.values()[c % TileType.values().length];
				float tx = c * (1 + gutter) + gutter;
				float ty = y * (1 + gutter) + gutter;
				Vector2 position = new Vector2(tx, ty);
				tileGraphics.add(new TileGraphic(position, tileType));
			}
		}
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
		ScreenUtils.clear(Color.LIGHT_GRAY);
		batch.begin();
		for(TileGraphic tileGraphic : tileGraphics) {
			tileGraphic.render(delta, batch);
		}
		if (Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			Vector3 worldPoint = camera.unproject(new Vector3(x, y, 0));
			Vector2 destination = new Vector2(worldPoint.x, worldPoint.y);
			for (TileGraphic tileGraphic: tileGraphics) {

				tileGraphic.handleCommand(new MoveTowards(destination));
			}
			lastKeyPressed = "CLICK " + x + "," + y;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			for (TileGraphic tileGraphic: tileGraphics) {
				Vector2 destination = new Vector2(MathUtils.random(0, GAME_WIDTH), MathUtils.random(0, GAME_HEIGHT));
				tileGraphic.handleCommand(new MoveTowards(destination));
			}
			lastKeyPressed = "ENTER";
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			for (TileGraphic tileGraphic: tileGraphics) {
				tileGraphic.handleCommand(new SelectTile(tileGraphic));
			}
			lastKeyPressed = "S";
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			for (TileGraphic tileGraphic: tileGraphics) {
				tileGraphic.handleCommand(new DeselectTile(tileGraphic));
			}
			lastKeyPressed = "D";
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			for (TileGraphic tileGraphic: tileGraphics) {
				tileGraphic.handleCommand(new IncludeInMatch(tileGraphic));
			}
			lastKeyPressed = "M";
		}
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 14, 7);
		font.draw(batch, lastKeyPressed, 14, 9);
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
