package space.peetseater.game.grid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.commands.ShiftToken;
import space.peetseater.game.grid.match.ColumnTileMatcher;
import space.peetseater.game.grid.match.FullGridTileMatcher;
import space.peetseater.game.grid.match.RowTileMatcher;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.ArrayList;
import java.util.LinkedList;
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

    @Test
    void swapValuesAt() {
        TileType tileType1 = grid.getTile(0, 0).getValue();
        TileType tileType2 = grid.getTile(0, 1).getValue();
        grid.swapValuesAt(0, 0, 0, 1);
        Assertions.assertEquals(tileType1, grid.getTile(0, 1).getValue());
        Assertions.assertEquals(tileType2, grid.getTile(0, 0).getValue());
    }

    @Test
    void testIfMovesInColumnValid() {
        grid.setTileValue(0, 0, TileType.HighValue);
        grid.setTileValue(1, 0, TileType.HighValue);
        grid.setTileValue(2, 0, TileType.LowValue);
        grid.setTileValue(3, 0, TileType.HighValue);

        LinkedList<ShiftToken> moves = new LinkedList<>();
        Assertions.assertFalse(grid.testIfMovesValid(moves, new ColumnTileMatcher<TileType>(grid, 0)));

        moves.add(new ShiftToken(2, 0, ShiftToken.Direction.UP, grid));
        Assertions.assertTrue(grid.testIfMovesValid(moves, new ColumnTileMatcher<TileType>(grid, 0)));
    }

    @Test
    void testIfMovesInRowValid() {
        for (int i = 0; i < grid.getWidth(); i++) {
            grid.setTileValue(0, i, TileType.LowValue);
            grid.setTileValue(1, i, i % 2 == 0 ? TileType.MidValue : TileType.Negative);
        }
        grid.setTileValue(0, 1, TileType.HighValue);

        LinkedList<ShiftToken> moves = new LinkedList<>();
        Assertions.assertFalse(grid.testIfMovesValid(moves, new RowTileMatcher<TileType>(grid, 0)));

        moves.add(new ShiftToken(0, 0, ShiftToken.Direction.RIGHT, grid));
        Assertions.assertTrue(grid.testIfMovesValid(moves, new RowTileMatcher<TileType>(grid, 0)));
    }


}