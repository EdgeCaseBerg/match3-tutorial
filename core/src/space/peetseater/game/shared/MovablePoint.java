package space.peetseater.game.shared;

import com.badlogic.gdx.math.Vector2;

public class MovablePoint implements Movable {

    protected Vector2 position;
    protected Vector2 destination;

    public MovablePoint(Vector2 position) {
        setPosition(position);
        setDestination(null);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }


    public Vector2 getDestination() {
        return destination;
    }

    public void setDestination(Vector2 destination) {
        this.destination = destination;
    }
}
