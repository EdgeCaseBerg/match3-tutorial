package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.ArrayList;
import java.util.List;

public class ColumnTileMatcher<T> extends AbstractMatcher<T> {
    private final int columnToCheck;

    public ColumnTileMatcher(GameGrid<T> grid, int column) {
        super(grid);
        this.columnToCheck = column;
    }

    @Override
    public List<Match<T>> findMatches() {
        List<GridSpace<T>> colSpaces = grid.getColumn(columnToCheck);
        List<Match<T>> matches = new ArrayList<>();

        for (int r = 0; r < colSpaces.size(); r++) {
            Match<T> m = search(r, columnToCheck);
            if (m.isLegal()) {
                matches.add(m);
                // Skip over any row already included in a match
                for (GridSpace<T> space : m.getSpaces()) {
                    r = Math.max(r, space.getRow());
                }
            }
        }
        return matches;
    }
}
