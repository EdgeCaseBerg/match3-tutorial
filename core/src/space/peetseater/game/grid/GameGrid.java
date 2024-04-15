package space.peetseater.game.grid;

import space.peetseater.game.grid.commands.ShiftToken;
import space.peetseater.game.grid.commands.ShiftToken.Direction;
import space.peetseater.game.grid.match.AbstractMatcher;
import space.peetseater.game.shared.Command;

import java.util.*;

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
        if (column < 0 || column >= width) {
            throw new SpaceOutOfBoundsException("Column index out of bound");
        }
        if (row >= height || row < 0) {
            throw new SpaceOutOfBoundsException("Row index out of bound");
        }
        return this.spaces.get(row).get(column);
    }

    public void setTileValue(int row, int column, T value) {
        getTile(row, column).setValue(value);
    }

    public void swapValuesAt(int aRow, int aColumn, int bRow, int bColumn) {
        T a = getTile(aRow, aColumn).getValue();
        T b = getTile(bRow, bColumn).getValue();
        setTileValue(aRow, aColumn, b);
        setTileValue(bRow, bColumn, a);
    }

    public List<ShiftToken> getShiftsToMoveFromStartToEnd(int startRow, int startColumn, int endRow, int endColumn) {
        boolean isHorizontal = startRow == endRow;
        boolean isVertical = startColumn == endColumn;
        Direction moveDirectionH = startColumn < endColumn ? Direction.RIGHT : Direction.LEFT;
        Direction moveDirectionV = startRow < endRow ? Direction.UP : Direction.DOWN;
        List<ShiftToken> moves = new LinkedList<>();
        if (isHorizontal) {
            for (int c = 0; c < Math.abs(startColumn - endColumn); c++) {
                int direction = Integer.signum(endColumn - startColumn);
                moves.add(new ShiftToken(startRow, startColumn + c *  direction, moveDirectionH, this));
            }
        } else if (isVertical) {
            for (int r = 0; r < Math.abs(startRow - endRow); r++) {
                int direction = Integer.signum(endRow - startRow);
                moves.add(new ShiftToken(startRow + r * direction, startColumn, moveDirectionV, this));
            }
        }
        return moves;
    }

    public void applyMoves(List<ShiftToken> moves) {
        for (Command move: moves) {
            move.execute();
        }
    }

    public boolean testIfMovesValid(
            List<ShiftToken> moves,
            AbstractMatcher<T> matcher
    ) {
        Stack<ShiftToken> applied = new Stack<>();
        boolean canApply = false;
        try {
            // Apply moves to grid
            for (ShiftToken move : moves) {
                move.execute();
                applied.push(move);
            }
            // Check for matches
            canApply = !matcher.findMatches().isEmpty();

            while (!applied.isEmpty()) {
                applied.pop().execute();
            }

            return canApply;
        } catch (InvalidShiftingException InvalidShiftingException) {
            // Undo previously applied moves to revert grid to prior state
            while(!applied.isEmpty()) {
                applied.pop().execute();
            }
            return canApply;
        }
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
        for (int r = 0; r < height; r++) {
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
