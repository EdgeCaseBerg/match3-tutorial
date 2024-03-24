package space.peetseater.game.token;

public interface TokenGeneratorAlgorithm<T> {
    abstract public T next(int row, int column);
}
