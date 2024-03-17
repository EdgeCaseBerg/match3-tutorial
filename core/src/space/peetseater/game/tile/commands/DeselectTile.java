package space.peetseater.game.tile.commands;

import space.peetseater.game.shared.Command;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.states.NotSelected;

public class DeselectTile implements Command {

    private final TileGraphic tileGraphic;

    public DeselectTile(TileGraphic tileGraphic) {
        this.tileGraphic = tileGraphic;
    }

    @Override
    public void execute() {
        tileGraphic.useNotSelectedTexture();
        tileGraphic.setState(NotSelected.getInstance());
    }
}
