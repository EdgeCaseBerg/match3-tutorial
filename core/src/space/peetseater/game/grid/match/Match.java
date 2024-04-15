package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GridSpace;

import java.util.LinkedList;
import java.util.Objects;

public class Match<T> {
    public static int DEFAULT_MIN_MATCH_LENGTH = 3;
    private final LinkedList<GridSpace<T>> spaces;
    private final LinkedList<T> values;
    private final int minMatchLength;

    public Match(GridSpace<T> seed, int minimumMatchLength) {
        this.spaces = new LinkedList<>();
        this.spaces.add(seed);
        this.values = new LinkedList<>();
        this.values.add(seed.getValue());
        this.minMatchLength = minimumMatchLength;
    }

    public Match(GridSpace<T> seed) {
        this(seed, DEFAULT_MIN_MATCH_LENGTH);
    }

    public boolean matches(GridSpace<T> other) {
        return other.equals(this.spaces.peek());
    }

    public void addMatch(GridSpace<T> space) {
        if (matches(space)) {
            this.values.add(space.getValue());
            this.spaces.add(space);
        }
    }

    public boolean isLegal() {
        return this.spaces.size() >= this.minMatchLength;
    }

    public LinkedList<T> getValues() {
        return this.values;
    }

    public LinkedList<GridSpace<T>> getSpaces() {
        return this.spaces;
    }

    public int getMinMatchLength() {
        return this.minMatchLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpaces());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Match) {
            Match<?> other = (Match<?>) obj;
            boolean eq = true;
            for (GridSpace<T> mySpace : this.spaces) {
                boolean matchInOther = other.spaces.contains(mySpace);
                eq = eq && matchInOther;
            }
            return eq;
        }
        return super.equals(obj);
    }
}
