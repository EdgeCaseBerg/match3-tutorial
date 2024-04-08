package space.peetseater.game.grid.match;

import com.badlogic.gdx.utils.viewport.FillViewport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.List;

class FullGridTileMatcherTest {
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
    void findMatches() {
        FullGridTileMatcher<TileType> matcher = new FullGridTileMatcher<>(grid);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(4, matches.size());

        // 0,0
        Match<TileType> lowValueMatch = matches.get(0);
        // 1,0
        Match<TileType> highValueMatch = matches.get(1);
        // 1,1
        Match<TileType> negativeMatch = matches.get(2);
        // 4,2
        Match<TileType> midValueMatch = matches.get(3);

        Assertions.assertTrue(lowValueMatch.isLegal());
        Assertions.assertTrue(highValueMatch.isLegal());
        Assertions.assertTrue(negativeMatch.isLegal());
        Assertions.assertTrue(midValueMatch.isLegal());

        for (GridSpace<TileType> space : lowValueMatch.getSpaces()) {
            int col = space.getColumn();
            Assertions.assertTrue(0 <= col && col <= 2);
            Assertions.assertEquals(0, space.getRow());
        }

        for (GridSpace<TileType> space : highValueMatch.getSpaces()) {
            int row = space.getRow();
            Assertions.assertTrue(1 <= row && row <= 3);
            Assertions.assertEquals(0, space.getColumn());
        }

        for (GridSpace<TileType> space : negativeMatch.getSpaces()) {
            int row = space.getRow();
            Assertions.assertTrue(1 <= row && row <= 3);
            Assertions.assertEquals(1, space.getColumn());
        }

        for (GridSpace<TileType> space : midValueMatch.getSpaces()) {
            int col = space.getColumn();
            Assertions.assertTrue(1 <= col && col <= 3);
            Assertions.assertEquals(4, space.getRow());
        }
    }
}