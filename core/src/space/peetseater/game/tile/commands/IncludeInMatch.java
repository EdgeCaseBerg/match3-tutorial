package space.peetseater.game.tile.commands;

import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.states.Matching;

public class IncludeInMatch implements Command {

    private final TileGraphic tileGraphic;

    public IncludeInMatch(TileGraphic tileGraphic) {
        this.tileGraphic = tileGraphic;
    }

    @Override
    public void execute() {
        tileGraphic.useMatchedTexture();
        tileGraphic.setState(Matching.getInstance());
    }
}

