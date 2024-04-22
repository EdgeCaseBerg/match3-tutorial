package space.peetseater.game.grid.states;

import space.peetseater.game.DragEventSubscriber;
import space.peetseater.game.states.Match3GameState;

public interface BoardState extends DragEventSubscriber {
    void onEnterState(Match3GameState match3GameState);
    void onExitState(Match3GameState match3GameState);
    BoardState update(float delta);
}
