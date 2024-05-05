package space.peetseater.game.grid.match;

import com.badlogic.gdx.math.MathUtils;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

abstract public class AbstractMatcher<T> {
    protected final GameGrid<T> grid;

    public AbstractMatcher(GameGrid<T> grid) {
        this.grid = grid;
    }

    public abstract List<Match<T>> findMatches();

    public Match<T> search(int row, int column) {
        return search(row, column, Match.DEFAULT_MIN_MATCH_LENGTH);
    }

    public Match<T> search(int row, int column, int minimumMatchLength) {
        GridSpace<T> space = grid.getTile(row, column);
        Match<T> m = new Match<>(space, minimumMatchLength);
        T value = space.getValue();
        if (null == value) {
            return m;
        }

        LinkedHashSet<GridSpace<T>> seen = new LinkedHashSet<>(3);
        Stack<GridSpace<T>> toVisit = new Stack<>();
        toVisit.push(space);
        while(!toVisit.isEmpty()) {
            GridSpace<T> curr = toVisit.pop();
            if (seen.contains(curr)) {
                continue;
            }
            seen.add(curr);
            if (!value.equals(curr.getValue())) {
                continue;
            }

            if (curr != space) {
                m.addMatch(curr);
            }

            int r = curr.getRow();
            int c = curr.getColumn();
            int aboveR = MathUtils.clamp(r + 1, 0, grid.getHeight() - 1);
            int belowR = MathUtils.clamp(r - 1, 0, grid.getHeight() - 1);
            int leftC = MathUtils.clamp(c - 1, 0, grid.getWidth() - 1);
            int rightC = MathUtils.clamp(c + 1, 0, grid.getWidth() - 1);

            toVisit.push(grid.getTile(aboveR, c));
            toVisit.push(grid.getTile(r, rightC));
            toVisit.push(grid.getTile(belowR, c));
            toVisit.push(grid.getTile(r, leftC));
        }

        if (m.isLegal()) {
            return m;
        }
        return new Match<>(space, minimumMatchLength);
    }
}
