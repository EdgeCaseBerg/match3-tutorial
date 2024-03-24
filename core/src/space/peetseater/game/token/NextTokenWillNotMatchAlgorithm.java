package space.peetseater.game.token;

import com.badlogic.gdx.math.MathUtils;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;

import java.util.Set;
import java.util.TreeSet;

public class NextTokenWillNotMatchAlgorithm<T> implements TokenGeneratorAlgorithm<T> {
    protected final Set<T> possibleValues;
    protected final GameGrid<T> constraintGrid;
    public NextTokenWillNotMatchAlgorithm(Set<T> possibleValues, GameGrid<T> grid) {
        this.possibleValues = possibleValues;
        this.constraintGrid = grid;
    }

    @Override
    public T next(int row, int column) {
        Set<T> possibleTypes = new TreeSet<>(possibleValues);
        Set<T> surroundingValues = new TreeSet<>();

        if (column - 1 >= 0) {
            GridSpace<T> tile = this.constraintGrid.getTile(row, column - 1);
            if (tile.isFilled()) {
                surroundingValues.add(tile.getValue());
            }
        }
        if (column < constraintGrid.getWidth() - 1) {
            GridSpace<T> tile = this.constraintGrid.getTile(row, column + 1);
            if (tile.isFilled()) {
                surroundingValues.add(tile.getValue());
            }
        }
        if (row - 1 >= 0) {
            GridSpace<T> tile = this.constraintGrid.getTile(row - 1, column);
            if (tile.isFilled()) {
                surroundingValues.add(tile.getValue());
            }
        }
        if (row < constraintGrid.getHeight() - 1) {
            GridSpace<T> tile = this.constraintGrid.getTile(row + 1, column);
            if (tile.isFilled()) {
                surroundingValues.add(tile.getValue());
            }
        }

        for (T t : surroundingValues) {
            possibleTypes.remove(t);
        }

        // Select a tile type from the remaining possible values;
        int tileIndex = MathUtils.random(possibleTypes.size() - 1);
        T nextTile = (T) possibleTypes.toArray()[tileIndex];
        return nextTile;
    }
}
