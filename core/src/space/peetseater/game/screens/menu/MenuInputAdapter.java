package space.peetseater.game.screens.menu;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.HashSet;

public class MenuInputAdapter extends InputAdapter {

    protected Viewport viewport;
    protected HashSet<MenuEventSubscriber> subscribers;

    public MenuInputAdapter(Viewport viewport) {
        this.viewport = viewport;
        this.subscribers = new HashSet<>(2);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean handled = false;
        for (MenuEventSubscriber dragEventSubscriber : subscribers) {
            handled = handled || dragEventSubscriber.onTouchDown();
        }
        return handled || super.touchDown(screenX, screenY, pointer, button);
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean handled = false;
        for (MenuEventSubscriber dragEventSubscriber : subscribers) {
            handled = handled || dragEventSubscriber.onTouchUp();
        }
        return handled || super.touchDown(screenX, screenY, pointer, button);
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 worldUnitVector = viewport.unproject(new Vector2(screenX, screenY));
        boolean handled = false;
        for (MenuEventSubscriber subscriber : subscribers) {
            handled = handled || subscriber.onMouseMove(worldUnitVector);
        }
        return handled || super.mouseMoved(screenX, screenY);
    }

    public void addSubscriber(MenuEventSubscriber... subscribers) {
        this.subscribers.addAll(Arrays.asList(subscribers));
    }
}
