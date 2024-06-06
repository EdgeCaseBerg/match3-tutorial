package space.peetseater.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.peetseater.game.DragEventSubscriber;

import java.util.HashSet;

public class DragInputAdapter extends InputAdapter {

    protected Viewport viewport;
    public Vector2 dragStart;
    public Vector2 dragEnd;
    public boolean isDragging;
    protected boolean wasDragged;

    protected HashSet<DragEventSubscriber> subscribers;

    public DragInputAdapter(Viewport viewport) {
        this.viewport = viewport;
        this.isDragging = false;
        this.wasDragged = false;
        this.subscribers = new HashSet<>(2);
    }

    public void addSubscriber(DragEventSubscriber dragEventSubscriber) {
        if (this.subscribers.contains(dragEventSubscriber)) {
            return;
        }
        this.subscribers.add(dragEventSubscriber);
    }


    protected boolean isLeftMouseClick(int pointer, int button) {
        // Right clicked / Whatever.
        if (Input.Buttons.LEFT != button) {
            return false;
        }

        // Player clicked / touched with a different finger. Ignore it.
        if (pointer != 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isLeftMouseClick(pointer, button)) {
            dragStart = viewport.unproject(new Vector2(screenX, screenY));
            this.isDragging = false;
            this.wasDragged = false;
            for (DragEventSubscriber dragEventSubscriber : subscribers) {
                dragEventSubscriber.onDragStart(dragStart.x, dragStart.y);
            }
            return true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isLeftMouseClick(pointer, Input.Buttons.LEFT)) {
            dragEnd = viewport.unproject(new Vector2(screenX, screenY));
            this.isDragging = true;
            for (DragEventSubscriber dragEventSubscriber : subscribers) {
                dragEventSubscriber.onDrag(dragEnd.x, dragEnd.y);
            }
            return true;
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isLeftMouseClick(pointer, button)) {
            dragEnd = viewport.unproject(new Vector2(screenX, screenY));
            if (this.isDragging) {
                this.wasDragged = true;
                for (DragEventSubscriber dragEventSubscriber : subscribers) {
                    dragEventSubscriber.onDragEnd(dragEnd.x, dragEnd.y);
                }
            } else {
                this.wasDragged = false;
                for (DragEventSubscriber dragEventSubscriber : subscribers) {
                    dragEventSubscriber.onLeftClick(dragEnd.x, dragEnd.y);
                }
            }
            this.isDragging = false;
            return true;
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    public Boolean getIsDragging() {
        return isDragging;
    }

    public boolean getWasDragged() {
        return wasDragged;
    }

    public boolean hasInputs() {
        return dragStart != null && dragEnd != null;
    }

}
