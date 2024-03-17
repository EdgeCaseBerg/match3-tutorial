package space.peetseater.game.tile.states;

import space.peetseater.game.tile.TileGraphicState;

public final class NotSelected extends TileGraphicState {

    private final static NotSelected instance = new NotSelected();

    public static NotSelected getInstance() {
        return instance;
    }

    private NotSelected() {
    }
}
