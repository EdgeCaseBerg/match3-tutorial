package space.peetseater.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.tile.TileType;

import java.util.LinkedList;
import java.util.List;

class ScoringCalculatorTest {
    ScoringCalculator calculator;
    @BeforeEach
    void beforeEach() {
         calculator = new ScoringCalculator();
    }

    Match<TileType> matchOfSize(int size, TileType tileType) {
        GridSpace<TileType> space = new GridSpace<>(0, 0, tileType);
        Match<TileType> match = new Match<>(space);
        for (int i = 1; i < size; i++) {
            match.addMatch(space);
        }
        return match;
    }

    @Test
    void onMatchesIncreasesScoreBy0OnNoMatches() {
        calculator.addToScore(new LinkedList<Match<TileType>>());
        Assertions.assertEquals(0, calculator.getScore());
    }

    @Test
    void onLowTokenAdd1PerTileToScore() {
        Match<TileType> match = matchOfSize(4, TileType.LowValue);
        List<Match<TileType>> matches = new LinkedList<>();
        matches.add(match);
        calculator.addToScore(matches);
        Assertions.assertEquals(4, calculator.getScore());
    }

    @Test
    void onMidTokenAdd2PerTileToScore() {
        Match<TileType> match = matchOfSize(4, TileType.MidValue);
        List<Match<TileType>> matches = new LinkedList<>();
        matches.add(match);
        calculator.addToScore(matches);
        Assertions.assertEquals(8, calculator.getScore());
    }

    @Test
    void onHighTokenAdd3PerTileToScore() {
        Match<TileType> match = matchOfSize(4, TileType.HighValue);
        List<Match<TileType>> matches = new LinkedList<>();
        matches.add(match);
        calculator.addToScore(matches);
        Assertions.assertEquals(12, calculator.getScore());
    }

    @Test
    void onMultiplierApplyToLastScore() {
        Match<TileType> multiplierMatch = matchOfSize(3, TileType.Multiplier);
        Match<TileType> pointMatch = matchOfSize(3, TileType.LowValue);

        List<Match<TileType>> firstMatch = new LinkedList<>();
        firstMatch.add(multiplierMatch);
        calculator.addToScore(firstMatch);
        Assertions.assertEquals(2, calculator.getMultiplier(), "did not add to multiplier");

        List<Match<TileType>> secondMatch = new LinkedList<>();
        secondMatch.add(pointMatch);
        calculator.addToScore(secondMatch);
        Assertions.assertEquals(6, calculator.getScore());
        Assertions.assertEquals(1, calculator.getMultiplier(), "Should reset the multipler after used");
    }

    @Test
    void onCombinedMatchWithMultiplierAndScoreApplyIt() {
        // Should generate a multipler + of 1 so we get x2
        Match<TileType> multiplierMatch = matchOfSize(3, TileType.Multiplier);
        Match<TileType> pointMatch = matchOfSize(3, TileType.LowValue);

        List<Match<TileType>> firstMatch = new LinkedList<>();
        firstMatch.add(pointMatch);
        firstMatch.add(multiplierMatch);

        calculator.addToScore(firstMatch);
        Assertions.assertEquals(6, calculator.getScore());
    }

    @Test
    void onCombinedMatchWithMultiplierAndScoreApplyItReverseOrder() {
        Match<TileType> multiplierMatch = matchOfSize(3, TileType.Multiplier);
        Match<TileType> pointMatch = matchOfSize(3, TileType.LowValue);

        List<Match<TileType>> firstMatch = new LinkedList<>();
        firstMatch.add(multiplierMatch);
        firstMatch.add(pointMatch);

        calculator.addToScore(firstMatch);
        Assertions.assertEquals(6, calculator.getScore());
    }

    @Test
    void multipliersStackUntilUsed() {
        Match<TileType> multiplierMatch1 = matchOfSize(3, TileType.Multiplier);
        Match<TileType> multiplierMatch2 = matchOfSize(3, TileType.Multiplier);
        Match<TileType> pointMatch = matchOfSize(3, TileType.LowValue);

        List<Match<TileType>> firstMatch = new LinkedList<>();
        firstMatch.add(multiplierMatch1);
        firstMatch.add(multiplierMatch2);
        calculator.addToScore(firstMatch);
        Assertions.assertEquals(3, calculator.getMultiplier(), "Should reset the multipler after used");

        List<Match<TileType>> secondMatch = new LinkedList<>();
        secondMatch.add(pointMatch);
        calculator.addToScore(secondMatch);
        Assertions.assertEquals(9, calculator.getScore());
        Assertions.assertEquals(1, calculator.getMultiplier(), "Should reset the multipler after used");
    }

    @Test
    void negativesShouldNotReduceScorePast0() {
        Match<TileType> match = matchOfSize(20, TileType.Negative);
        List<Match<TileType>> firstMatch = new LinkedList<>();
        firstMatch.add(match);
        calculator.addToScore(firstMatch);
        Assertions.assertEquals(0, calculator.getScore());
    }

    @Test
    void negativesWillReduceCombinedMatch() {
        List<Match<TileType>> firstMatch = new LinkedList<>();
        firstMatch.add(matchOfSize(3, TileType.Negative));
        firstMatch.add(matchOfSize(4, TileType.MidValue));
        calculator.addToScore(firstMatch);
        Assertions.assertEquals(2, calculator.getScore());
    }

    @Test
    void negativesCanBeMultiplied() {
        List<Match<TileType>> matches = new LinkedList<>();
        matches.add(matchOfSize(20, TileType.LowValue));
        calculator.addToScore(matches);

        matches.clear();
        matches.add(matchOfSize(3, TileType.Negative));
        matches.add(matchOfSize(3, TileType.Multiplier));
        calculator.addToScore(matches);

        Assertions.assertEquals(8, calculator.getScore());
        Assertions.assertEquals(1, calculator.getMultiplier());
    }
}