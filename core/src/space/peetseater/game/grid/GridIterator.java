package space.peetseater.game.grid;

import java.util.Iterator;

public class GridIterator<T> implements Iterator<GridSpace<T>> {

    private final GameGrid<T> gameGrid;
    private int r;
    private int c;

    public GridIterator(GameGrid<T> gameGrid) {
        this.gameGrid = gameGrid;
        this.r = 0;
        this.c = 0;
    }

    @Override
    public boolean hasNext() {
        return this.r < gameGrid.getHeight() && this.c < gameGrid.getWidth();
    }

    @Override
    public GridSpace<T> next() {
        GridSpace<T> gridSpace = gameGrid.getTile(r, c);
        c++;
        if (c >= gameGrid.getWidth()) {
            r++;
            c = 0;
        }

        return gridSpace;
    }

    @Override
    public void remove() {
        GridSpace<T> gridSpace = gameGrid.getTile(r, c);
        gridSpace.setValue(null);
    }
}
