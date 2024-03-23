package space.peetseater.game.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameGrid<T> implements Iterable<GridSpace<T>>{
    protected final List<List<GridSpace<T>>> spaces;
    protected final int width;
    protected final int height;

    public GameGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.spaces = new ArrayList<>();
        for (int r = 0; r < height; r++) {
            this.spaces.add(new ArrayList<GridSpace<T>>());
            for (int c = 0; c < width; c++) {
                this.spaces.get(r).add(new GridSpace<T>(r, c));
            }
        }
    }

    public GridSpace<T> getTile(int row, int column) {
        if (column < 0 || column > width) {
            throw new SpaceOutOfBoundsException("Column index out of bound");
        }
        if (row > height || row < 0) {
            throw new SpaceOutOfBoundsException("Row index out of bound");
        }
        return this.spaces.get(row).get(column);
    }

    public void setTileValue(int row, int column, T value) {
        getTile(row, column).setValue(value);
    }

    public List<GridSpace<T>> getRow(int row) {
        if (row > height || row < 0) {
            throw new SpaceOutOfBoundsException("Row index out of bound");
        }
        return this.spaces.get(row);
    }

    public List<GridSpace<T>> getColumn(int column) {
        if (column < 0 || column > width) {
            throw new SpaceOutOfBoundsException("Column index out of bound");
        }
        List<GridSpace<T>> columnSpaces = new ArrayList<>();
        for (int r = 0; r < width; r++) {
            columnSpaces.add(this.spaces.get(r).get(column));
        }
        return columnSpaces;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public Iterator<GridSpace<T>> iterator() {
        return new GridIterator<T>(this);
    }
}
