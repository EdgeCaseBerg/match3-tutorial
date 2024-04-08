package space.peetseater.game.grid.match;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.GameGrid;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;
import space.peetseater.game.token.TokenGeneratorAlgorithm;

import java.util.List;

class AbstractMatcherTest {
    GameGrid<TileType> grid;
    AbstractMatcher<TileType> matcher;

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
        matcher = new AbstractMatcher<TileType>(grid) {
            @Override
            public List<Match<TileType>> findMatches() {
                return null;
            }
        };
    }


    @Test
    void checkMatchesAbove() {
        Match<TileType> match = matcher.search(1, 0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getValues().size());
        Assertions.assertEquals(1, match.getSpaces().get(0).getRow());
        Assertions.assertEquals(2, match.getSpaces().get(1).getRow());
        Assertions.assertEquals(3, match.getSpaces().get(2).getRow());
    }

    @Test
    void checkMatchesBelow() {
        Match<TileType> match = matcher.search(3, 0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getValues().size());
        Assertions.assertEquals(3, match.getSpaces().get(0).getRow());
        Assertions.assertEquals(2, match.getSpaces().get(1).getRow());
        Assertions.assertEquals(1, match.getSpaces().get(2).getRow());
    }

    @Test
    void checkMatchesLeft() {
        Match<TileType> match = matcher.search(4, 3);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getValues().size());
        Assertions.assertEquals(3, match.getSpaces().get(0).getColumn());
        Assertions.assertEquals(2, match.getSpaces().get(1).getColumn());
        Assertions.assertEquals(1, match.getSpaces().get(2).getColumn());
    }

    @Test
    void checkMatchesRight() {
        Match<TileType> match = matcher.search(0, 0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getValues().size());
        Assertions.assertEquals(0, match.getSpaces().get(0).getColumn());
        Assertions.assertEquals(1, match.getSpaces().get(1).getColumn());
        Assertions.assertEquals(2, match.getSpaces().get(2).getColumn());
    }

    @Test
    void checkMatchesHorizontallyCenteredAt() {
        Match<TileType> match = matcher.search(4, 2);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getValues().size());
        for (GridSpace<TileType> found :match.getSpaces()) {
            int col = found.getColumn();
            Assertions.assertTrue(1 <= col && col <= 3);
            Assertions.assertEquals(4, found.getRow());
        }
    }

    @Test
    void checkMatchesVerticallyCenteredAt() {
        Match<TileType> match = matcher.search(2, 0);
        Assertions.assertTrue(match.isLegal());
        Assertions.assertEquals(3, match.getValues().size());
    }

}