package space.peetseater.game;

public interface DragEventSubscriber {
    public void onDragStart(float gameX, float gameY);

    public void onDrag(float gameX, float gameY);

    public void onDragEnd(float gameX, float gameY);

    public void onLeftClick(float gameX, float gameY);
}
