package space.peetseater.game.grid.match;

public interface MatchEventPublisher<T> {
    public void addSubscriber(MatchSubscriber<T> matchSubscriber);
    public void removeSubscriber(MatchSubscriber<T> matchSubscriber);
}
