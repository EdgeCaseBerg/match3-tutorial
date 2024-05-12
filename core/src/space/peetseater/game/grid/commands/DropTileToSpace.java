package space.peetseater.game.grid.commands;

import space.peetseater.game.shared.Command;
import space.peetseater.game.states.Match3GameState;
import space.peetseater.game.tile.TileType;

public class DropTileToSpace implements Command {
    private final Match3GameState match3GameState;
    private final int row;
    private final int column;

    public DropTileToSpace(
            Match3GameState match3GameState, int row, int column
    ) {
        this.match3GameState = match3GameState;
        this.row = row;
        this.column = column;
    }

    @Override
    public void execute() {
        TileType newTileType = match3GameState.getTokenAlgorithm().next(row, column);
        match3GameState.getGameGrid().setTileValue(row, column, newTileType);
        match3GameState.getBoardGraphic().replaceTile(row, column, newTileType);
    }
}
