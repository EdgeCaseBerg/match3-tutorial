package space.peetseater.game.shared;

import com.badlogic.gdx.math.Vector2;

public interface Movable {

    public Vector2 getPosition();
    public void setPosition(Vector2 position);

    public Vector2 getDestination();

    public void setDestination(Vector2 destination);
}
