package space.peetseater.game.states;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import space.peetseater.game.shared.Command;
import space.peetseater.game.shared.Movable;
import space.peetseater.game.shared.commands.MoveTowards;

public class LinearMovementBehavior {
    protected Movable movable;
    protected float secondsToSpendMoving = 1f;
    protected float elapsedTime = 0;

    public LinearMovementBehavior(Movable movable) {
        this.movable = movable;
    }

    public void update(float delta) {
        if (!movable.getPosition().equals(movable.getDestination()) && movable.getDestination() != null) {
            elapsedTime += delta;
            float alpha = MathUtils.clamp(elapsedTime / secondsToSpendMoving, 0, 1);
            movable.getPosition().interpolate(movable.getDestination(), alpha, Interpolation.linear);

            if(movable.getPosition().equals(movable.getDestination())) {
                elapsedTime = 0;
            }
        }
    }

    public Command handleCommand(Command command) {
        if (command instanceof MoveTowards) {
            ((MoveTowards) command).setMovable(movable);
            return command;
        }
        return null;
    }

}
