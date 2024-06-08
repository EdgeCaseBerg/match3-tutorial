package space.peetseater.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameSettings {
    private static GameSettings _this;
    private final Preferences preferences;
    private float bgmVolume;
    private float sfxVolume;
    private GameDifficulty difficult;

    public static GameSettings getInstance() {
        if (_this == null) {
            _this = new GameSettings();
        }
        return _this;
    }

    private GameSettings() {
        this.preferences = Gdx.app.getPreferences("RELAXNMATCH");
        this.bgmVolume = this.preferences.getFloat("bgmVolume", 1f);
        this.sfxVolume = this.preferences.getFloat("sfxVolume", 1f);
        this.difficult = GameDifficulty.valueOf(this.preferences.getString("difficulty", GameDifficulty.NORMAL.name()));
    }

    public float getBgmVolume() {
        return bgmVolume;
    }
    public synchronized void setBgmVolume(float bgmVolume) {
        this.preferences.putFloat("bgmVolume", bgmVolume);
        this.bgmVolume = bgmVolume;
        this.preferences.flush();
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public synchronized void setSfxVolume(float sfxVolume) {
        this.preferences.putFloat("sfxVolume", sfxVolume);
        this.sfxVolume = sfxVolume;
        this.preferences.flush();
    }

    public GameDifficulty getDifficult() {
        return difficult;
    }

    public synchronized void setDifficult(GameDifficulty difficult) {
        this.preferences.putString("difficulty", difficult.name());
        this.difficult = difficult;
        this.preferences.flush();
    }
}
