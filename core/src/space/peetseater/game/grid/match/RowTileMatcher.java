package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.ArrayList;
import java.util.List;

public class RowTileMatcher<T> extends AbstractMatcher<T> {
    protected final int rowToCheck;

    public RowTileMatcher(GameGrid<T> grid, int row) {
        super(grid);
        this.rowToCheck = row;
    }

    @Override
    public List<Match<T>> findMatches() {
        List<GridSpace<T>> rowSpaces = grid.getRow(rowToCheck);
        List<Match<T>> matches = new ArrayList<>();

        for (int c = 0; c < rowSpaces.size(); c++) {
            Match<T> m = search(rowToCheck, c);
            if (m.isLegal()) {
                matches.add(m);
                // Skip over any row already included in a match
                for (GridSpace<T> space : m.getSpaces()) {
                    c = Math.max(c, space.getColumn());
                }
            }
        }
        return matches;
    }
}
