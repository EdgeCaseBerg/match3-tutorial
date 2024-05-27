package space.peetseater.game.screens.transitions;

public interface TransitionStep {
    void update(float delta);
    void render(float delta);
    boolean isComplete();
    void onEnd();
    void onStart();
}
