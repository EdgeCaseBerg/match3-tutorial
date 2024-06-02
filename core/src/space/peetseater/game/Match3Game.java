package space.peetseater.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import space.peetseater.game.screens.*;
import space.peetseater.game.screens.transitions.Transition;
import space.peetseater.game.screens.transitions.TransitionType;

import java.util.Stack;

public class Match3Game extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Music bgm;
	public Match3Assets match3Assets;
	public Stack<Scene> scenes;
	Transition transition;
	@Override
	public void create () {
		scenes = new Stack<>();
		this.batch = new SpriteBatch();
		match3Assets = new Match3Assets();
		loadAssets();
		// Play one tune the whole game for now
		this.bgm.setVolume(GameSettings.getInstance().getBgmVolume());
		this.bgm.setLooping(true);
		this.bgm.play();
		transition = new Transition(this, TransitionType.Push, null, new TitleScreen(this));
	}

	@Override
	public void render() {
		if (transition == null) {
			super.render();
			return;
		}

		if (!transition.isStarted()) {
			transition.onStart();
			setScreen(transition);
		}

		float delta = Gdx.graphics.getDeltaTime();
		transition.update(delta);
		transition.render(delta);

		if (transition.isComplete()) {
			Gdx.app.log("STACK", scenes.toString());
			switch (transition.transitionType) {
                case Push:
					scenes.push(transition.to);
                    break;
                case Replace:
					scenes.pop().dispose();
					scenes.push(transition.to);
                    break;
                case Pop:
					scenes.pop().dispose();
                    break;
            }
			setScreen(scenes.peek());
			transition.onEnd();
			Gdx.app.log("STACK", scenes.toString());
			transition = null;
		}
	}

	public void replaceSceneWith(Scene scene) {
		transition = new Transition(this, TransitionType.Replace, scenes.peek(), scene);
	}

	public void overlayScene(Scene scene) {
		transition = new Transition(this, TransitionType.Push, scenes.peek(), scene);
	}

	public void closeOverlaidScene() {
		Scene leaving = scenes.pop();
		transition = new Transition(this, TransitionType.Pop, leaving, scenes.peek());
		scenes.push(leaving);
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
		for (Scene scene : scenes) {
			scene.dispose();
		}
		match3Assets.unloadFont();
		match3Assets.unloadBGM();
		match3Assets.dispose();
	}
}
