package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.shared.Command;

public class RepositionCrossSection implements Command {
    private final BoardManager boardManager;
    private final int row;
    private final int column;

    public RepositionCrossSection(BoardManager boardManager, int row, int column) {
        this.boardManager = boardManager;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        this.boardManager.repositionCrossSection(row, column);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
            return row;
        }

}
