package space.peetseater.game.tile;

import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.commands.DeselectTile;
import space.peetseater.game.tile.commands.IncludeInMatch;
import space.peetseater.game.tile.commands.SelectTile;

public abstract class TileGraphicState {

    protected float timeInState;

    public void update(float delta) {
        timeInState += delta;
    }

    public Command handleCommand(Command command) {
        if (command instanceof SelectTile) {
            return command;
        } else if (command instanceof DeselectTile) {
            return command;
        } else if (command instanceof IncludeInMatch) {
            return command;
        }
        return null;
    }

    public float getTimeInState() {
        return timeInState;
    }
}
