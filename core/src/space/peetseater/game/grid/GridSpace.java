package space.peetseater.game.grid;

import java.util.Objects;

public class GridSpace<T> {
    private int row;
    private int column;
    private T value;

    public GridSpace(int row, int column) {
        this.row = row;
        this.column = column;
        this.value = null;
    }
    public GridSpace(int row, int column, T value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public boolean isFilled() {
        return value != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridSpace<T> filled = (GridSpace<T>) o;
        return Objects.equals(value, filled.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, row, column);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
