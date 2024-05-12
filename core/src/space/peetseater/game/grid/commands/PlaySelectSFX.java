package space.peetseater.game.grid.commands;

import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.shared.Command;

public class PlaySelectSFX implements Command {
    private final BoardManager boardManager;

    public PlaySelectSFX(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Override
    public void execute() {
        this.boardManager.playSelectSFX();
    }
}
