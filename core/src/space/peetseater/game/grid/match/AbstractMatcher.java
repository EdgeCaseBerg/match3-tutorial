package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.commands.ShiftToken;

import java.util.List;

abstract public class AbstractMatcher<T> {
    protected final GameGrid<T> grid;

    public AbstractMatcher(GameGrid<T> grid) {
        this.grid = grid;
    }

    public abstract List<Match<T>> findMatches();

    public void _searchDir(int row, int column, int depth, ShiftToken.Direction direction, Match<T> currentMatch) {
        if (row < 0 || column < 0 || row > grid.getHeight() - 1 || column > grid.getWidth() - 1) {
            // We are out of bounds.
            return;
        }

        GridSpace<T> space = grid.getTile(row, column);
        if (!currentMatch.matches(space)) {
            return;
        }

        if (depth >= currentMatch.getMinMatchLength() * 2) {
            return;
        }

        currentMatch.addMatch(space);

        switch (direction) {
            case UP:
                _searchDir(row + 1, column,  + 1, direction, currentMatch);
                break;
            case DOWN:
                _searchDir(row - 1, column,  + 1, direction, currentMatch);
                break;
            case LEFT:
                 _searchDir(row, column - 1,  + 1, direction, currentMatch);
                break;
            case RIGHT:
                _searchDir(row, column + 1,  + 1, direction, currentMatch);
                break;
        }
    }

    public Match<T> search(int row, int column) {
        return search(row, column, Match.DEFAULT_MIN_MATCH_LENGTH);
    }

    public Match<T> search(int row, int column, int minimumMatchLength) {
        GridSpace<T> space = grid.getTile(row, column);
        Match<T> vMatch = new Match<>(space, minimumMatchLength);
        Match<T> hMatch = new Match<>(space, minimumMatchLength);
        _searchDir(row  + 1, column, 1, ShiftToken.Direction.UP, vMatch);
        _searchDir(row - 1, column, 1, ShiftToken.Direction.DOWN, vMatch);
        _searchDir(row, column - 1, 1, ShiftToken.Direction.LEFT, hMatch);
        _searchDir(row, column + 1, 1, ShiftToken.Direction.RIGHT, hMatch);
        Match<T> m = new Match<>(space);
        if (vMatch.isLegal()) {
            for (GridSpace<T> gs : vMatch.getSpaces()) {
                if (gs != space) {
                    m.addMatch(gs);
                }
            }
        }
        if (hMatch.isLegal()) {
            for (GridSpace<T> gs : hMatch.getSpaces()) {
                if (gs != space) {
                    m.addMatch(gs);
                }
            }
        }
        return m;
    }
}
