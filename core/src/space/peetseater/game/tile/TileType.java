package space.peetseater.game.tile;

public enum TileType {
    LowValue,
    MidValue,
    HighValue,
    Multiplier,
    Negative;

    static public int scoreFor(TileType tileType) {
        switch (tileType) {
            case LowValue:
                return 1;
            case MidValue:
                return 2;
            case HighValue:
                return 3;
            case Multiplier:
                return 0;
            case Negative:
                return -2;
        }
        return 0;
    }
}
