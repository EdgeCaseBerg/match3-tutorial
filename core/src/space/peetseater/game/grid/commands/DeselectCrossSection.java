package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.shared.Command;

public class DeselectCrossSection implements Command {

    private final BoardManager boardManager;
    private final int row;
    private final int column;

    public DeselectCrossSection(BoardManager boardManager, int row, int column) {
        this.boardManager = boardManager;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        this.boardManager.deselectCrossSection(row, column);
    }
}

