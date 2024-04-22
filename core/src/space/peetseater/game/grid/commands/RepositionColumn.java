package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.shared.Command;

public class RepositionColumn implements Command {

    private final BoardGraphic boardGraphic;
    private final int column;

    public RepositionColumn(int column, BoardGraphic boardGraphic) {
        this.boardGraphic = boardGraphic;
        this.column = column;
    }

    @Override
    public void execute() {
        boardGraphic.repositionColumn(column);
    }
}

