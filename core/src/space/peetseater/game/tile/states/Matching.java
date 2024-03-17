package space.peetseater.game.tile.states;

import space.peetseater.game.tile.TileGraphicState;

public class Matching extends TileGraphicState {

    private final static Matching instance = new Matching();

    public static Matching getInstance() {
        return instance;
    }

    private Matching() {
    }
}
