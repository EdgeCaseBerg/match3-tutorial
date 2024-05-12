package space.peetseater.game.shared.commands;

import com.badlogic.gdx.audio.Sound;
import space.peetseater.game.shared.Command;

public class PlaySound implements Command {

    private final Sound sound;

    public PlaySound(Sound sound) {
        this.sound = sound;
    }


    @Override
    public void execute() {
        // Prevent excess volume by ensure we're not already playing this sound.
        sound.stop();
        sound.play();
    }
}
