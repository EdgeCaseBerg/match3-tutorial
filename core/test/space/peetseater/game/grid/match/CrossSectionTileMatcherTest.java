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

class CrossSectionTileMatcherTest {
    GameGrid<TileType> grid;

    TokenGeneratorAlgorithm<TileType> fixedTokenAlgo = new TokenGeneratorAlgorithm<TileType>() {
        final TileType[] tokens = {
                LowValue, LowValue, LowValue, HighValue, HighValue, HighValue,
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
    void findMatchesOverlappingWithTileType() {
        CrossSectionTileMatcher<TileType> matcher = new CrossSectionTileMatcher<TileType>(1, 1, grid);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(3, matches.size());
    }
}