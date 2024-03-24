package space.peetseater.game.token;

import com.badlogic.gdx.math.MathUtils;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** Simple algorithm that will return a tile based on what other tiles are in the row,
 *  likely to match with another tile in the row.
 *
 * */
public class NextTokenLikelyToMatchAlgorithm<T> implements TokenGeneratorAlgorithm<T> {
    protected final Set<T> possibleValues;
    protected final GameGrid<T> constraintGrid;
    public NextTokenLikelyToMatchAlgorithm(Set<T> possibleValues, GameGrid<T> grid) {
        this.possibleValues = possibleValues;
        this.constraintGrid = grid;
    }

    @Override
    public T next(int row, int column) {
        Set<T> surroundingValues = new TreeSet<>();
        List<GridSpace<T>> colSpaces = this.constraintGrid.getColumn(column);
        List<GridSpace<T>> rowSpaces = this.constraintGrid.getRow(row);

        for (GridSpace<T> space : colSpaces) {
            if (space.isFilled()) {
                surroundingValues.add(space.getValue());
            }
        }
        for (GridSpace<T> space : rowSpaces) {
            if (space.isFilled()) {
                surroundingValues.add(space.getValue());
            }
        }
        // Edge case for a board with a lot of empty spaces.
        if (surroundingValues.isEmpty() || surroundingValues.size() < possibleValues.size()) {
            surroundingValues.addAll(possibleValues);
        }
        return (T) surroundingValues.toArray()[MathUtils.random(0, surroundingValues.size() - 1)];
    }
}
