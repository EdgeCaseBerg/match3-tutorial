package space.peetseater.game.tile.commands;

import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.states.Selected;

public class SelectTile implements Command {

    private final TileGraphic tileGraphic;

    public SelectTile(TileGraphic tileGraphic) {
        this.tileGraphic = tileGraphic;
    }

    @Override
    public void execute() {
        tileGraphic.useSelectedTexture();
        tileGraphic.setState(Selected.getInstance());
    }
}
