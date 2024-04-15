package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GameGrid;

import java.util.*;

public class CrossSectionTileMatcher<T> extends AbstractMatcher<T> {
    private RowTileMatcher<T> rowTileMatcher;
    private ColumnTileMatcher<T> columnTileMatcher;

    public CrossSectionTileMatcher(int row, int column, GameGrid<T> gameGrid) {
        super(gameGrid);
        this.rowTileMatcher = new RowTileMatcher<>(gameGrid, row);
        this.columnTileMatcher = new ColumnTileMatcher<>(gameGrid, column);
    }

    @Override
    public List<Match<T>> findMatches() {
        List<Match<T>> rowMatches = rowTileMatcher.findMatches();
        List<Match<T>> columnMatches = columnTileMatcher.findMatches();

        // If we have matches from both, make sure they're not the same match
        List<Match<T>> matches = new ArrayList<>(rowMatches.size() + columnMatches.size());
        if (!rowMatches.isEmpty() && !columnMatches.isEmpty()) {
            Set<Match<T>> deduplicationSet = new HashSet<>();
            deduplicationSet.addAll(rowMatches);
            deduplicationSet.addAll(columnMatches);
            matches.addAll(deduplicationSet);
        } else {
            matches.addAll(rowMatches);
            matches.addAll(columnMatches);
        }
        return matches;
    }
}
