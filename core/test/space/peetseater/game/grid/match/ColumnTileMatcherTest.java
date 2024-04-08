package space.peetseater.game.grid.match;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.List;

import static space.peetseater.game.tile.TileType.*;

class ColumnTileMatcherTest {
    GameGrid<TileType> grid;

    TokenGeneratorAlgorithm<TileType> fixedTokenAlgo = new TokenGeneratorAlgorithm<TileType>() {
        final TileType[] tokens = {
                LowValue, LowValue, LowValue, HighValue, LowValue, HighValue,
                HighValue, Negative, Negative, Negative, LowValue, LowValue,
                HighValue, Negative, HighValue, MidValue, HighValue, HighValue,
                HighValue, Negative, MidValue, HighValue, LowValue, LowValue,
                LowValue, HighValue, Negative, Negative, LowValue, HighValue
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
        grid = new GameGrid<>(6, 5);
        for (GridSpace<TileType> space : grid) {
            space.setValue(fixedTokenAlgo.next(0, 0));
        }
    }

    @Test
    void findMatchesAt00And30() {
        ColumnTileMatcher<TileType> matcher = new ColumnTileMatcher<>(grid, 0);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(2, matches.size());

        Match<TileType> match = matches.get(0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getSpaces().size());
        for (GridSpace<TileType> space :match.getSpaces()) {
            int col = space.getColumn();
            Assertions.assertTrue(0 <= col && col <= 2);
            Assertions.assertEquals(0, space.getRow());
        }

        Match<TileType> match2 = matches.get(1);
        Assertions.assertTrue(match2.isLegal());
        Assertions.assertEquals(3, match2.getSpaces().size());
        for (GridSpace<TileType> space :match2.getSpaces()) {
            int row = space.getRow();
            Assertions.assertTrue(1 <= row && row <= 3);
            Assertions.assertEquals(0, space.getColumn());
        }
    }

    @Test
    void findNoMatchesInColumn4() {
        ColumnTileMatcher<TileType> matcher = new ColumnTileMatcher<>(grid, 4);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(0, matches.size());
    }

    @Test
    void find2MatchesInColumn1() {
        ColumnTileMatcher<TileType> matcher = new ColumnTileMatcher<>(grid, 1);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(2, matches.size());
        Match<TileType> match = matches.get(0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getSpaces().size());

        for (GridSpace<TileType> space :match.getSpaces()) {
            int column = space.getColumn();
            Assertions.assertTrue(0 <= column && column <= 2);
            Assertions.assertEquals(0, space.getRow());
        }

        Match<TileType> bigMatch = matches.get(1);
        Assertions.assertTrue(bigMatch.isLegal());
        Assertions.assertEquals(5, bigMatch.getSpaces().size());
    }
}