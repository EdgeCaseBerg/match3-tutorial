package space.peetseater.game.screens.menu;

import com.badlogic.gdx.math.Vector2;

public interface MenuEventSubscriber {
    public boolean onMouseMove(Vector2 point);

    public boolean onTouchDown();

    public boolean onTouchUp();
}
