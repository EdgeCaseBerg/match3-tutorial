package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.shared.Command;

public class PlaySelectSFX implements Command {
    private final BoardGraphic boardGraphic;

    public PlaySelectSFX(BoardGraphic boardGraphic) {
        this.boardGraphic = boardGraphic;
    }

    @Override
    public void execute() {
        this.boardGraphic.playSelectSFX();
    }
}
