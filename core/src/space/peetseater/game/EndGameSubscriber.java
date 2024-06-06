package space.peetseater.game;

public interface EndGameSubscriber {
    void notifyGameShouldEnd(GameStats gameStats);
}
