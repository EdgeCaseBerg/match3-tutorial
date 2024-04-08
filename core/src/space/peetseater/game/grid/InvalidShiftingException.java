package space.peetseater.game.grid;

import space.peetseater.game.grid.commands.ShiftToken;

public class InvalidShiftingException extends RuntimeException {
    public InvalidShiftingException(ShiftToken shiftToken, Throwable cause) {
        super("Move " + shiftToken + " was invalid", cause);
    }
}
