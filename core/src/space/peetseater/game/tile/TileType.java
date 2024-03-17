package space.peetseater.game.tile;

import com.badlogic.gdx.graphics.Color;

public enum TileType {
    LowValue,
    MidValue,
    HighValue,
    Multiplier,
    Negative;

    static public Color colorFor(TileType tileType) {
        switch (tileType) {
            case LowValue:
                return Color.GREEN;
            case MidValue:
                return Color.BLUE;
            case HighValue:
                return  Color.RED;
            case Multiplier:
                return Color.YELLOW;
            case Negative:
                return Color.BROWN;
        }
        return Color.CHARTREUSE;
    }
}
