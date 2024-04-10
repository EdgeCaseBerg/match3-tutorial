package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.InvalidShiftingException;
import space.peetseater.game.grid.SpaceOutOfBoundsException;
import space.peetseater.game.shared.Command;
import java.util.Objects;

public class ShiftToken implements Command {

    public static enum Direction {
        UP, RIGHT, DOWN, LEFT
    }
    public int startRow;
    public int startColumn;
    protected GameGrid gameGrid;
    public Direction moveDirection;


    public ShiftToken(int startRow, int startColumn, Direction moveDirection, GameGrid gameGrid) {
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.moveDirection = moveDirection;
        this.gameGrid = gameGrid;

        try {
            gameGrid.getTile(startRow, startColumn);
        } catch (SpaceOutOfBoundsException tileOutOfBoundsException) {
            throw new InvalidShiftingException(this, tileOutOfBoundsException);
        }
    }

    public String toString() {
        return "(" + this.startRow + "," + this.startColumn + ") D:" + moveDirection;
    }

    public void execute() {
        int endRow;
        int endColumn;
        switch (moveDirection) {
            case UP:
                endRow = startRow + 1;
                endColumn = startColumn;
                break;
            case DOWN:
                endRow = startRow - 1;
                endColumn = startColumn;
                break;
            case LEFT:
                endRow = startRow;
                endColumn = startColumn - 1;
                break;
            case RIGHT:
            default:
                endRow = startRow;
                endColumn = startColumn + 1;
                break;
        }

        try {
            gameGrid.swapValuesAt(startRow, startColumn, endRow, endColumn);
        } catch (SpaceOutOfBoundsException tileOutOfBoundsException) {
            throw new InvalidShiftingException(this, tileOutOfBoundsException);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftToken move = (ShiftToken) o;
        return startRow == move.startRow && startColumn == move.startColumn && moveDirection == move.moveDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startRow, startColumn, moveDirection);
    }
}

