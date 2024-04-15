package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.shared.Command;

public class RepositionCrossSection implements Command {
    private final BoardGraphic boardGraphic;
    private final int row;
    private final int column;

    public RepositionCrossSection(BoardGraphic boardGraphic, int row, int column) {
        this.boardGraphic = boardGraphic;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        this.boardGraphic.repositionCrossSection(row, column);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
            return row;
        }

}
