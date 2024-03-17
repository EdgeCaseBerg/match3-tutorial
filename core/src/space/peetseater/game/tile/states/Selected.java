package space.peetseater.game.tile.states;

import space.peetseater.game.tile.TileGraphicState;

public final class Selected extends TileGraphicState {

    private static final Selected instance = new Selected();

    public static Selected getInstance() {
        return instance;
    }

    private Selected() {
    }
}
