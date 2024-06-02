package space.peetseater.game.screens.menu;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import space.peetseater.game.Match3Assets;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class VolumeControl extends MenuButton implements ButtonListener, MenuEventSubscriber {
    private final List<PlateButton> volumeButtons;
    private float volume;
    private HashSet<ButtonListener> listeners;

    public VolumeControl(String text, float startingVolume, Vector2 position, Vector2 minimumSize, Match3Assets match3Assets) {
        super(text, position, minimumSize, match3Assets);
        volumeButtons = new LinkedList<>();
        listeners = new HashSet<>();
        this.volume = startingVolume;
        Vector2 plateSize = new Vector2(0.5f, 0.5f);
        Vector2 plateStart = position.cpy().sub(0, 1);
        for (int i = 0; i < 10; i++) {
            Vector2 pos = plateStart.cpy().add(i * 0.5f, 0);
            PlateButton plateButton = new PlateButton("", pos, plateSize, match3Assets);
            plateButton.setToggled(i < volume*10);
            plateButton.addButtonListener(this);
            volumeButtons.add(plateButton);
        }
    }

    public void render(float delta, SpriteBatch spriteBatch, BitmapFont font) {
        font.draw(spriteBatch, getText(), position.x, position.y, minSize.x, Align.center, false);
        for (PlateButton plateButton : volumeButtons) {
            plateButton.render(delta, spriteBatch, font);
        }
    }

    public void addButtonListener(ButtonListener buttonListener) {
        listeners.add(buttonListener);
    }

    @Override
    public void buttonClicked(MenuButton menuButton) {
        int idx = volumeButtons.indexOf(menuButton);
        if (idx == -1) {
            return;
        }
        this.volume = (float) idx / (volumeButtons.size() - 1);
        int i = 0;
        for (PlateButton plateButton : volumeButtons) {
            plateButton.setToggled(i < volume*10f);
            i++;
        }
        for (ButtonListener listener : listeners) {
            listener.buttonClicked(this);
        }
    }

    @Override
    public boolean onMouseMove(Vector2 point) {
        boolean handled = false;
        for (PlateButton plateButton : volumeButtons) {
            handled = plateButton.onMouseMove(point) || handled;
        }
        return handled;
    }

    @Override
    public boolean onTouchDown() {
        boolean handled = false;
        for (PlateButton plateButton : volumeButtons) {
            handled =  plateButton.onTouchDown() || handled;
        }
        return handled;
    }

    @Override
    public boolean onTouchUp() {
        boolean handled = false;
        for (PlateButton plateButton : volumeButtons) {
            handled =  plateButton.onTouchUp() || handled;
        }
        return handled;
    }

    public float getVolume() {
        return this.volume;
    }
}
