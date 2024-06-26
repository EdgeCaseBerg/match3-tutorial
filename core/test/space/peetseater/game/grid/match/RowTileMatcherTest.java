package space.peetseater.game.grid.match;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static space.peetseater.game.tile.TileType.*;

class RowTileMatcherTest {
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
    void findMatchesAt00And30() {
        RowTileMatcher<TileType> matcher = new RowTileMatcher<>(grid, 0);
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
            int col = space.getColumn();
            Assertions.assertTrue(3 <= col && col <= 5);
            Assertions.assertEquals(0, space.getRow());
        }
    }

    @Test
    void findNoMatchesInRow4() {
        RowTileMatcher<TileType> matcher = new RowTileMatcher<>(grid, 4);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(0, matches.size());
    }

    @Test
    void find2MatchesInRow1() {
        RowTileMatcher<TileType> matcher = new RowTileMatcher<>(grid, 1);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(2, matches.size());
        Match<TileType> match = matches.get(0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getSpaces().size());
        for (GridSpace<TileType> space :match.getSpaces()) {
            int row = space.getRow();
            Assertions.assertTrue(1 <= row && row <= 3);
            Assertions.assertEquals(0, space.getColumn());
        }

        Match<TileType> bigMatch = matches.get(1);
        Assertions.assertTrue(bigMatch.isLegal());
        Assertions.assertEquals(5, bigMatch.getSpaces().size());
    }

    @Test
    void findMatchesWhenProcessingEntireRow() {
        RowTileMatcher<TileType> matcher = new RowTileMatcher<>(grid, 2);
        List<Match<TileType>> matches = matcher.findMatches();
        Assertions.assertEquals(2, matches.size());

        final HashMap<GridSpace<TileType>, Boolean> expectedSeen = new HashMap<>();
        expectedSeen.put(grid.getTile(1, 0), false);
        expectedSeen.put(grid.getTile(2, 0), false);
        expectedSeen.put(grid.getTile(3, 0), false);
        expectedSeen.put(grid.getTile(1, 1), false);
        expectedSeen.put(grid.getTile(2, 1), false);
        expectedSeen.put(grid.getTile(3, 1), false);
        expectedSeen.put(grid.getTile(1, 2), false);
        expectedSeen.put(grid.getTile(1, 3), false);

        for (Match<TileType> match : matches) {
            for (GridSpace<TileType> gridSpace : match.getSpaces()) {
                expectedSeen.put(gridSpace, true);
            }
        }

        List<Executable> tests = new ArrayList<>();
        for (final GridSpace<TileType> key : expectedSeen.keySet()) {
            tests.add(
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        Assertions.assertTrue(expectedSeen.get(key), key.getRow() + "," + key.getColumn() + "  not seen in match");
                    }
                }
            );
        }
        Assertions.assertAll(tests);
    }


}