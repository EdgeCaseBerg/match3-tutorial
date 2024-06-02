package space.peetseater.game.screens.menu;

import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.Match3Assets;

public class ToggleButton extends MenuButton {
    protected String altText;

    protected boolean isToggled = false;

    public ToggleButton(String startText, String toggledText, Vector2 position, Vector2 minimumSize, Match3Assets match3Assets) {
        super(startText, position, minimumSize, match3Assets);
        this.altText = toggledText;
        this.isToggled = false;
    }

    @Override
    public boolean onTouchDown() {
       if (super.onTouchDown()) {
           setToggled(!isToggled());
       }
       return false;
    }

    @Override
    public String getText() {
        if (isToggled()) {
            return this.altText;
        }
        return super.getText();
    }

    public boolean isToggled() {
        return isToggled;
    }

    public void setToggled(boolean toggled) {
        isToggled = toggled;
    }
}
