package space.peetseater.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.peetseater.game.screens.PlayScreen;

public class Match3Game extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Music bgm;
	public Match3Assets match3Assets;
	PlayScreen playScreen;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		match3Assets = new Match3Assets();
		loadAssets();
		// Play one tune the whole game for now
		this.bgm.setVolume(0.6f);
		this.bgm.setLooping(true);
		this.bgm.play();
		playScreen = new PlayScreen(this);
		setScreen(playScreen);
	}

	public void loadAssets() {
		match3Assets.loadEssentialAssets();
		this.font = match3Assets.getFont();
		this.bgm = match3Assets.getBGM();

	}

	@Override
	public void dispose () {
		batch.dispose();
		playScreen.dispose();
		match3Assets.unloadBGM();
		match3Assets.dispose();
	}
}
