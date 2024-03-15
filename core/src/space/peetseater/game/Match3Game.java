package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.LinkedList;
import java.util.List;

public class Match3Game extends ApplicationAdapter {
	SpriteBatch batch;
	private List<TileGraphic> tileGraphics;

	@Override
	public void create () {
		batch = new SpriteBatch();
		tileGraphics = new LinkedList<>();
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
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
