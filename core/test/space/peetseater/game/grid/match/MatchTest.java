package space.peetseater.game.grid.match;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.tile.TileType;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void acceptMinHorizontal() {
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 1, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 2, TileType.HighValue));
        Assertions.assertTrue(match.isLegal());
    }

    @Test
    void acceptMoreThanMinHorizontal() {
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 1, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 2, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 3, TileType.HighValue));
        Assertions.assertTrue(match.isLegal());
    }

    @Test
    void rejectLessThanMinHorizontal() {
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 1, TileType.HighValue));
        Assertions.assertFalse(match.isLegal());
    }

    @Test
    void acceptMinVertical() {
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(1, 0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(2, 0, TileType.HighValue));
        Assertions.assertTrue(match.isLegal());
    }

    @Test
    void acceptMoreThanMinVertical() {
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(1, 0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(2, 0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(3, 0, TileType.HighValue));
        Assertions.assertTrue(match.isLegal());
    }

    @Test
    void rejectLessThanMinVertical() {
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(1, 0, TileType.HighValue));
        Assertions.assertFalse(match.isLegal());
    }

    @Test
    void trimSegmentsLowerThanMinHorizontal() {
        GridSpace<TileType> widow = new GridSpace<TileType>(0, 1, TileType.HighValue);
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(1, 0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(2, 0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(3, 0, TileType.HighValue));
        match.addMatch(widow);
        Assertions.assertTrue(match.isLegal());
        for (GridSpace<TileType> space : match.getSpaces()) {
            boolean sameLocationR = widow.getRow() == space.getRow();
            boolean sameLocationC = widow.getColumn() == space.getColumn();
            Assertions.assertFalse(sameLocationR && sameLocationC);
        }
    }

    @Test
    void trimSegmentsLowerThanMinVertical() {
        GridSpace<TileType> widow = new GridSpace<TileType>(1, 1, TileType.HighValue);
        Match<TileType> match = new Match<>(new GridSpace<TileType>(0,0, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 1, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 2, TileType.HighValue));
        match.addMatch(new GridSpace<TileType>(0, 3, TileType.HighValue));
        match.addMatch(widow);
        Assertions.assertTrue(match.isLegal());
        for (GridSpace<TileType> space : match.getSpaces()) {
            boolean sameLocationR = widow.getRow() == space.getRow();
            boolean sameLocationC = widow.getColumn() == space.getColumn();
            Assertions.assertFalse(sameLocationR && sameLocationC);
        }
    }
}