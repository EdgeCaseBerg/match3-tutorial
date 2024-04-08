package space.peetseater.game.grid.match;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.List;

class SingleTileMatcherTest {
    GameGrid<TileType> grid;

    TokenGeneratorAlgorithm<TileType> fixedTokenAlgo = new TokenGeneratorAlgorithm<TileType>() {
        final TileType[] tokens = {
                TileType.LowValue, TileType.LowValue, TileType.LowValue, TileType.MidValue,
                TileType.HighValue, TileType.Negative, TileType.MidValue, TileType.HighValue,
                TileType.HighValue, TileType.Negative, TileType.HighValue, TileType.MidValue,
                TileType.HighValue, TileType.Negative, TileType.MidValue, TileType.HighValue,
                TileType.LowValue, TileType.MidValue, TileType.MidValue, TileType.MidValue,
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
    void findMatchesAt00() {
        SingleTileMatcher<TileType> matcher = new SingleTileMatcher<>(grid, grid.getTile(0,0));
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(1, matches.size());
        Match<TileType> match = matches.get(0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getSpaces().size());
        for (GridSpace<TileType> space :match.getSpaces()) {
            int col = space.getColumn();
            Assertions.assertTrue(0 <= col && col <= 2);
            Assertions.assertEquals(0, space.getRow());
        }
    }

    @Test
    void findNoMatchesAt22() {
        SingleTileMatcher<TileType> matcher = new SingleTileMatcher<>(grid, grid.getTile(2,2));
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(0, matches.size());
    }

    @Test
    void findMatchesAt20() {
        SingleTileMatcher<TileType> matcher = new SingleTileMatcher<>(grid, grid.getTile(2,0));
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(1, matches.size());
        Match<TileType> match = matches.get(0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getSpaces().size());
        for (GridSpace<TileType> space :match.getSpaces()) {
            int row = space.getRow();
            Assertions.assertTrue(1 <= row && row <= 3);
            Assertions.assertEquals(0, space.getColumn());
        }
    }

    @Test
    void findMatchesAt42() {
        SingleTileMatcher<TileType> matcher = new SingleTileMatcher<>(grid, grid.getTile(4,2));
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(1, matches.size());
        Match<TileType> match = matches.get(0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getSpaces().size());
        for (GridSpace<TileType> space :match.getSpaces()) {
            int col = space.getColumn();
            Assertions.assertTrue(1 <= col && col <= 3);
            Assertions.assertEquals(4, space.getRow());
        }
    }
}