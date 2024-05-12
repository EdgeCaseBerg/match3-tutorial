package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.shared.Command;

public class SelectCrossSection implements Command {

    private final BoardManager boardManager;
    private final int row;
    private final int column;

    public SelectCrossSection(BoardManager boardManager, int row, int column) {
        this.boardManager = boardManager;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        this.boardManager.selectCrossSection(row, column);
    }

    public Command undoCommand() {
        return new DeselectCrossSection(boardManager, row, column);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
