package space.peetseater.game.shared.commands;

import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.Movable;

public class MoveTowards implements Command {

    private final Vector2 destination;
    private Movable movable;

    public MoveTowards(Vector2 destination) {
        this.destination = destination;
    }

    public MoveTowards(Vector2 destination, Movable movable) {
        this(destination);
        setMovable(movable);
    }

    public Vector2 getDestination() {
        return destination;
    }

    public void setMovable(Movable movable) {
        this.movable = movable;
    }

    @Override
    public void execute() {
        if (null != movable) {
            movable.setDestination(destination);
        }
    }
}
