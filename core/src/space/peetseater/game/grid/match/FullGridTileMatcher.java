package space.peetseater.game.grid.match;

import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FullGridTileMatcher<T> extends AbstractMatcher<T> {
    public FullGridTileMatcher(GameGrid<T> grid) {
        super(grid);
    }

    @Override
    public List<Match<T>> findMatches() {
        List<Match<T>> matches = new ArrayList<>();
        HashSet<GridSpace<T>> seen = new HashSet<>(grid.getWidth() * grid.getHeight());
        for (GridSpace<T> space : grid) {
            if (seen.contains(space)) {
                continue;
            }
            Match<T> m = search(space.getRow(), space.getColumn());
            if (m.isLegal()) {
                matches.add(m);
                for (GridSpace<T> s : m.getSpaces()) {
                    seen.add(s);
                }
            }
            seen.add(space);
        }
        return matches;
    }
}
