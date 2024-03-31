package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.shared.Command;

public class DeselectCrossSection implements Command {

    private final BoardGraphic boardGraphic;
    private final int row;
    private final int column;

    public DeselectCrossSection(BoardGraphic boardGraphic, int row, int column) {
        this.boardGraphic = boardGraphic;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        this.boardGraphic.deselectCrossSection(row, column);
    }
}

