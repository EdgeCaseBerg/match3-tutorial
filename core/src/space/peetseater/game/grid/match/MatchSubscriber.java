package space.peetseater.game.grid.match;

import java.util.List;

public interface MatchSubscriber<T> {
    public void onMatches(List<Match<T>> matches);
}
