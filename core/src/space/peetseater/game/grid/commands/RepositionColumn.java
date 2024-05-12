package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.shared.Command;

public class RepositionColumn implements Command {

    private final BoardManager boardManager;
    private final int column;

    public RepositionColumn(int column, BoardManager boardManager) {
        this.boardManager = boardManager;
        this.column = column;
    }

    @Override
    public void execute() {
        boardManager.repositionColumn(column);
    }
}

