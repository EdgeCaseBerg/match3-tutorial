package space.peetseater.game;

public class Constants {
    public static final int GAME_HEIGHT = 10;
    public static final int GAME_WIDTH = 16;
    static public float TILE_UNIT_WIDTH = 1;
    static public float TILE_UNIT_HEIGHT = 1;

    static public int TOKENS_PER_ROW = 7;
    static public int TOKENS_PER_COLUMN = 8;

    static public float BOARD_UNIT_GUTTER = 0.2f;
    static public float BOARD_UNIT_WIDTH = TOKENS_PER_ROW * TILE_UNIT_WIDTH + (TOKENS_PER_ROW - 1) * BOARD_UNIT_GUTTER + BOARD_UNIT_GUTTER * 2;
    static public float BOARD_UNIT_HEIGHT = TOKENS_PER_COLUMN * TILE_UNIT_HEIGHT + (TOKENS_PER_COLUMN - 1) * BOARD_UNIT_GUTTER + BOARD_UNIT_GUTTER * 2;

    static public float SCORE_UNIT_WIDTH = TILE_UNIT_WIDTH * 4 + BOARD_UNIT_GUTTER;
    static public float SCORE_UNIT_HEIGHT = TILE_UNIT_HEIGHT + BOARD_UNIT_GUTTER;


}
