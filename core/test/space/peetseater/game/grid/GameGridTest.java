package space.peetseater.game.grid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.commands.ShiftToken;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.List;

class GameGridTest {

    GameGrid<TileType> grid;

    TokenGeneratorAlgorithm<TileType> fixedTokenAlgo = new TokenGeneratorAlgorithm<TileType>() {
        final TileType[] tokens = {
                TileType.LowValue, TileType.Multiplier, TileType.HighValue, TileType.MidValue,
                TileType.HighValue, TileType.Negative, TileType.MidValue, TileType.HighValue,
        };
        int i = 0;
        @Override
        public TileType next(int row, int column) {
            TileType tileType = tokens[i];
            i = (i + 1) % (tokens.length);
            return tileType;
        }
    };

    @BeforeEach
    void setUp() {
        grid = new GameGrid<>(4, 5);
        for (GridSpace<TileType> space : grid) {
            space.setValue(fixedTokenAlgo.next(0, 0));
        }
    }

    @Test
    void getShiftsToMoveFromStartToEnd00To03() {
        int sr = 0;
        int sc = 0;
        int er = 0;
        int ec = grid.width - 1;
        List<ShiftToken> moves = grid.getShiftsToMoveFromStartToEnd(sr, sc, er, ec);
        Assertions.assertNotEquals(0, moves.size());
        Assertions.assertEquals(grid.width - 1, moves.size());
        for (int i = 0; i < moves.size(); i++) {
            ShiftToken move = moves.get(i);
            Assertions.assertEquals(ShiftToken.Direction.RIGHT, move.moveDirection);
            Assertions.assertEquals(i, move.startColumn);
        }
    }

    @Test
    void getShiftsToMoveFromStartToEnd00To40() {
        int sr = 0;
        int sc = 0;
        int er = grid.getHeight() - 1;
        int ec = 0;
        List<ShiftToken> moves = grid.getShiftsToMoveFromStartToEnd(sr, sc, er, ec);
        Assertions.assertNotEquals(0, moves.size());
        Assertions.assertEquals(grid.height - 1, moves.size());
        for (int i = 0; i < moves.size(); i++) {
            ShiftToken move = moves.get(i);
            Assertions.assertEquals(ShiftToken.Direction.UP, move.moveDirection);
            Assertions.assertEquals(i, move.startRow);
        }
    }

    @Test
    void getShiftsToMoveFromStartToEnd03To00() {
        int sr = 0;
        int sc = grid.width - 1;
        int ec = 0;
        int er = 0;
        List<ShiftToken> moves = grid.getShiftsToMoveFromStartToEnd(sr, sc, er, ec);
        Assertions.assertNotEquals(0, moves.size());
        Assertions.assertEquals(grid.width - 1, moves.size());
        for (int i = sc; i > 0; i--) {
            ShiftToken move = moves.get(sc - i);
            Assertions.assertEquals(ShiftToken.Direction.LEFT, move.moveDirection);
            Assertions.assertEquals(i, move.startColumn);
        }
    }

    @Test
    void getShiftsToMoveFromStartToEnd40To00() {
        int sc = 0;
        int sr = grid.getHeight() - 1;
        int ec = 0;
        int er = 0;
        List<ShiftToken> moves = grid.getShiftsToMoveFromStartToEnd(sr, sc, er, ec);
        Assertions.assertNotEquals(0, moves.size());
        Assertions.assertEquals(grid.height - 1, moves.size());
        for (int i = 0; i < moves.size(); i++) {
            ShiftToken move = moves.get(i);
            Assertions.assertEquals(ShiftToken.Direction.DOWN, move.moveDirection);
            Assertions.assertEquals(sr - i, move.startRow);
        }
    }

    @Test
    void getShiftsToMoveFromStartToEnd0011() {
        int sc = 0;
        int sr = 0;
        int ec = 1;
        int er = 1;
        List<ShiftToken> moves = grid.getShiftsToMoveFromStartToEnd(sc, sr, ec, er);
        Assertions.assertEquals(0, moves.size());
    }
}