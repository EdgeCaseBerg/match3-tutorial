package space.peetseater.game;

import com.badlogic.gdx.math.MathUtils;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.tile.TileType;

import java.util.List;

public class ScoringCalculator {

    private int score;
    private int multiplier;
    public ScoringCalculator() {
        this.score = 0;
        this.multiplier = 1;
    }

    public int getScore() {
        return score;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void resetScore() {
        setScore(0);
    }

    public void addToScore(List<Match<TileType>> matches) {
        if (matches.isEmpty()) {
            return;
        }

        int matchScore = 0;
        int negativeScore = 0;
        for (Match<TileType> match : matches) {
            if (match.getValues().peek().equals(TileType.Multiplier)) {
                multiplier += (match.getValues().size() - match.getMinMatchLength() + 1);
            }
            for (TileType tileType : match.getValues()) {
                switch (tileType) {
                    case LowValue:
                    case MidValue:
                    case HighValue:
                        matchScore += TileType.scoreFor(tileType); break;
                    case Negative:
                        negativeScore += 2;
                        break;
                    case Multiplier:
                    default:
                        // no op
                }
            }
        }
        score += (matchScore - negativeScore) * multiplier;
        setScore(MathUtils.clamp(score, 0, Integer.MAX_VALUE));
        // Reset multiplier if we just used it.
        if (multiplier != 1 && matchScore > 0 || negativeScore != 0) {
            multiplier = 1;
        }
    }
}
