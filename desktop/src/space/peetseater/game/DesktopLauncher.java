package space.peetseater.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import space.peetseater.game.Match3Game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(Match3Game.GAME_WIDTH * 72, Match3Game.GAME_HEIGHT * 72);
		config.setTitle("Match3Tutorial");
		new Lwjgl3Application(new Match3Game(), config);
	}
}
