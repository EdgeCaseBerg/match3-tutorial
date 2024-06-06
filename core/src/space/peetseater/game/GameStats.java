package space.peetseater.game;

public class GameStats {
    private final int score;
    private final int moves;

    public GameStats(int movesCompleted, int finalScore) {
        this.moves = movesCompleted;
        this.score = finalScore;
    }

    public int getScore() {
        return score;
    }

    public int getMoves() {
        return moves;
    }
}
