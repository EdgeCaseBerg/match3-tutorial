package space.peetseater.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.peetseater.game.screens.LoadingScreen;
import space.peetseater.game.screens.PlayScreen;

public class Match3Game extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Music bgm;
	public Match3Assets match3Assets;
	PlayScreen playScreen;
	LoadingScreen loadingScreen;
	Screen currentScreen;

	@Override
	public void create () {
		this.batch = new SpriteBatch();
		match3Assets = new Match3Assets();
		loadAssets();
		// Play one tune the whole game for now
		this.bgm.setVolume(0.6f);
		this.bgm.setLooping(true);
		this.bgm.play();
		match3Assets.queueAssets();
		loadingScreen = new LoadingScreen(this);
		playScreen = new PlayScreen(this);
		currentScreen = playScreen;
		setScreen(loadingScreen);
	}

	@Override
	public void render() {
		boolean doneLoading = match3Assets.assetManager.update(1);
		if (!doneLoading) {
			if (screen != loadingScreen) {
				setScreen(loadingScreen);
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			match3Assets.assetManager.clear();
			loadAssets();
			match3Assets.queueAssets();
		}

		if (loadingScreen.isLoadingComplete()) {
			if (screen != currentScreen) {
				loadingScreen.setHasConfirmedLoading(false);
				setScreen(currentScreen);
			}
		}
		super.render();
	}

	public void loadAssets() {
		match3Assets.queueBGM();
		match3Assets.queueFont();
		match3Assets.blockingLoad();
		this.font = match3Assets.getFont();
		this.bgm = match3Assets.getBGM();
	}

	@Override
	public void dispose () {
		batch.dispose();
		playScreen.dispose();
		loadingScreen.dispose();
		match3Assets.unloadBGM();
		match3Assets.dispose();
	}
}
