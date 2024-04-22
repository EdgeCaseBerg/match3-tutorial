package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.shared.Command;

public class ClearGridSpace implements Command {
    private final GridSpace<?> gridSpace;
    public ClearGridSpace(GridSpace<?> gridSpace) {
        this.gridSpace = gridSpace;
    }

    @Override
    public void execute() {
        gridSpace.setValue(null);
    }
}
