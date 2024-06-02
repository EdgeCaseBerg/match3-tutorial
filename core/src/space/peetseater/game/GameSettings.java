package space.peetseater.game;

public class GameSettings {
    private static GameSettings _this;
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
        this.bgmVolume = 1f;
        this.sfxVolume = 1f;
        this.difficult = GameDifficulty.NORMAL;
    }

    public float getBgmVolume() {
        return bgmVolume;
    }
    public synchronized void setBgmVolume(float bgmVolume) {
        this.bgmVolume = bgmVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public synchronized void setSfxVolume(float sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public GameDifficulty getDifficult() {
        return difficult;
    }

    public synchronized void setDifficult(GameDifficulty difficult) {
        this.difficult = difficult;
    }

}
