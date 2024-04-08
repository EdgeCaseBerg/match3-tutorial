package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.ArrayList;
import java.util.List;

public class SingleTileMatcher<T> extends AbstractMatcher<T> {
    private final GridSpace<T> tile;

    public SingleTileMatcher(GameGrid<T> grid, GridSpace<T> tile) {
        super(grid);
        this.tile = tile;
    }

    @Override
    public List<Match<T>> findMatches() {
        List<Match<T>> matches = new ArrayList<>(2);
        Match<T> m = search(tile.getRow(), tile.getColumn());
        if (m.isLegal()) {
            matches.add(m);
        }
        return matches;
    }
}
