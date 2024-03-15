package space.peetseater.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Match3Game extends ApplicationAdapter {
	SpriteBatch batch;
	private TileGraphic tileGraphic;

	@Override
	public void create () {
		batch = new SpriteBatch();
		tileGraphic = new TileGraphic(Vector2.Zero.add(30,30), TileType.HighValue);
	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.LIGHT_GRAY);
		batch.begin();
		tileGraphic.render(Gdx.graphics.getDeltaTime(), batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
