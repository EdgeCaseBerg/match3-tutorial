package space.peetseater.game.screens.transitions;

public enum TransitionType {
    // Add to scene stack, do not remove current scene
    // i.e Loading screen, Pause screen, etc
    Push,
    // Pop off old scene and discard, add new scene
    // Title -> Game Screen, Level 1 to Level 2, etc
    Replace,
    // Pop off old scene and discard, let whatever is on the top of the stack become the current
    // i.e: unpause
    Pop,
}
