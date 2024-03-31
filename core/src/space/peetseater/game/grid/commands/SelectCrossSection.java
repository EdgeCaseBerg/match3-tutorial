package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.shared.Command;

public class SelectCrossSection implements Command {

    private final BoardGraphic boardGraphic;
    private final int row;
    private final int column;

    public SelectCrossSection(BoardGraphic boardGraphic, int row, int column) {
        this.boardGraphic = boardGraphic;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        this.boardGraphic.selectCrossSection(row, column);
    }

    public Command undoCommand() {
        return new DeselectCrossSection(boardGraphic, row, column);
    }

}
