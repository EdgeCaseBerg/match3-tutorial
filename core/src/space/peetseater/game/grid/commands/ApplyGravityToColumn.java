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
        Gdx.app.log("Apply Gravity", "Gets moves " + moves.size());
        for (ShiftToken move : moves) {
            Gdx.app.log("Apply Gravity", move.toString());
            move.execute();
        }
    }
}
