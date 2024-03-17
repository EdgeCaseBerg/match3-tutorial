package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
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

	@Override
	public void create () {
		batch = new SpriteBatch();
		tileGraphics = new LinkedList<>();
		font = new BitmapFont();
		for (int c = 0; c < TileType.values().length; c++) {
			TileType tileType = TileType.values()[c];
			tileGraphics.add(new TileGraphic(new Vector2((c * TileGraphic.TILESIZE) + 15 * (c+1), 50), tileType));
		}
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		ScreenUtils.clear(Color.LIGHT_GRAY);
		batch.begin();
		for(TileGraphic tileGraphic : tileGraphics) {
			tileGraphic.render(delta, batch);
		}
		if (Gdx.input.isTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			for (TileGraphic tileGraphic: tileGraphics) {
				Vector2 destination = new Vector2(x, y);
				tileGraphic.handleCommand(new MoveTowards(destination));
			}
			lastKeyPressed = "CLICK " + x + "," + y;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			for (TileGraphic tileGraphic: tileGraphics) {
				Vector2 destination = new Vector2(MathUtils.random(0, 300), MathUtils.random(0, 300));
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
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 20);
		font.draw(batch, lastKeyPressed, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
