package space.peetseater.game.grid.commands;

import com.badlogic.gdx.Gdx;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.shared.Command;

import java.util.List;

public class ApplyGravityToColumn implements Command {
    private final int column;
    private final GameGrid<?> gameGrid;

    public ApplyGravityToColumn(int column, GameGrid<?> gameGrid) {
        this.column = column;
        this.gameGrid = gameGrid;
    }

    @Override
    public void execute() {
        List<ShiftToken> moves = gameGrid.getGravityShiftsForColumn(column);
        for (ShiftToken move : moves) {
            move.execute();
        }
    }
}
