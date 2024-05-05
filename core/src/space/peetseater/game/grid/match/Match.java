package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GridSpace;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
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
        LinkedHashMap<Integer, Integer> numberOfSpacesByRow = new LinkedHashMap<>();
        LinkedHashMap<Integer, Integer> numberOfSpacesByColumn = new LinkedHashMap<>();
        for (GridSpace<T> space : this.spaces) {
            int columnCount = numberOfSpacesByColumn.getOrDefault(space.getColumn(), 0);
            int rowCount = numberOfSpacesByRow.getOrDefault(space.getRow(), 0);
            numberOfSpacesByColumn.put(space.getColumn(), ++columnCount);
            numberOfSpacesByRow.put(space.getRow(), ++rowCount);
        }

        this.values.clear();
        ListIterator<GridSpace<T>> listIterator = this.spaces.listIterator();
        while(listIterator.hasNext()) {
            GridSpace<T> space = listIterator.next();
            int numSpacesSharingColumn = numberOfSpacesByColumn.get(space.getColumn());
            int numSpacesSharingRow = numberOfSpacesByRow.get(space.getRow());
            boolean partOfHorizontalMatch = numSpacesSharingColumn >= minMatchLength;
            boolean partOfVerticalMatch = numSpacesSharingRow >= minMatchLength;
            if (partOfVerticalMatch || partOfHorizontalMatch) {
                values.add(space.getValue());
            } else {
                listIterator.remove();
            }
        }

        return this.spaces.size() >= minMatchLength;
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
