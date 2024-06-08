package space.peetseater.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import space.peetseater.game.grid.BoardManager;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.grid.match.MatchSubscriber;
import space.peetseater.game.grid.states.MoveEventSubscriber;
import space.peetseater.game.screens.menu.BackgroundPanel;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ScoreManager implements MatchSubscriber<TileType>, Disposable, MoveEventSubscriber {

    private final ScoringCalculator scoringCalculator;
    private final MovablePoint movablePoint;
    private final BoardManager boardManager;
    private final LinkedList<TileGraphic> inFlightMatches;
    private final Match3Assets match3Assets;
    private final BackgroundPanel bg;
    private int movesCompleted;
    private int movesToGo;
    private int targetScore;
    private final HashSet<EndGameSubscriber> endGameSubscribers;

    public ScoreManager(Vector2 position, BoardManager boardManager, Match3Assets match3Assets) {
        this.scoringCalculator = new ScoringCalculator();
        inFlightMatches = new LinkedList<>();
        this.movablePoint = new MovablePoint(position);
        endGameSubscribers = new HashSet<>(1);
        this.bg = new BackgroundPanel(
            position.cpy().sub(0, 1 + Constants.BOARD_UNIT_GUTTER * 2),
            new Vector2(
                5,
                Constants.SCORE_UNIT_HEIGHT * 2 + Constants.BOARD_UNIT_GUTTER
            ),
            match3Assets
        );
        // TODO I don't like that I take this in to calc where to start
        // the tile graphics in flight path to the board. We can revisit
        // this after making some form of class for the match particles
        this.boardManager = boardManager;
        this.match3Assets = match3Assets;
        movesCompleted = 0;
        movesToGo = 20;
        targetScore = 100;
    }
    public void render(float delta, SpriteBatch spriteBatch, BitmapFont bitmapFont) {
        float cornerX = movablePoint.getPosition().x;
        float cornerY = movablePoint.getPosition().y;
        bg.render(delta, spriteBatch);
        bitmapFont.draw(
                spriteBatch,
                "Score: " + scoringCalculator.getScore() +
                    "\n" + "Multiplier: x" + scoringCalculator.getMultiplier() +
                    "\nMoves left: " + movesToGo +
                    "\nTarget Score: " + targetScore,
                cornerX + Constants.BOARD_UNIT_GUTTER,
                cornerY + Constants.SCORE_UNIT_HEIGHT - Constants.BOARD_UNIT_GUTTER
        );

        for (TileGraphic tileGraphic : inFlightMatches) {
            tileGraphic.render(delta, spriteBatch);
        }
    }

    public void update(float delta) {
        ListIterator<TileGraphic> iter = inFlightMatches.listIterator();
        while (iter.hasNext()) {
            TileGraphic tileGraphic = iter.next();
            if (tileGraphic.getMovablePoint().isAtDestination()) {
                switch (tileGraphic.getTileType()) {
                    case Multiplier:
                        Sound multiplierSFX = match3Assets.getMultiplierSFX();
                        multiplierSFX.stop();
                        multiplierSFX.play(GameSettings.getInstance().getSfxVolume());
                        break;
                    case Negative:
                        Sound negativeSFX = match3Assets.getNegativeSFX();
                        negativeSFX.stop();
                        negativeSFX.play(GameSettings.getInstance().getSfxVolume());
                        break;
                    default:
                        Sound scoreUpSFX = match3Assets.getIncrementScoreSFX();
                        scoreUpSFX.stop();
                        scoreUpSFX.play(GameSettings.getInstance().getSfxVolume());
                }
                iter.remove();
            }
            tileGraphic.update(delta);
        }
    }

    @Override
    public void onMatches(List<Match<TileType>> matches) {
        // TODO: make not tile graphics, but some other type
        for (Match<TileType> match : matches) {
            for (GridSpace<?> space : match.getSpaces()) {
                // Move the tile graphics to the score window
                float ty = boardManager.screenYFromGridRow(space.getRow());
                float tx = boardManager.screenXFromGridColumn(space.getColumn());
                TileGraphic tileGraphic = new TileGraphic(new Vector2(tx, ty), match.getValues().get(0), match3Assets);
                tileGraphic.getMovablePoint().setDestination(movablePoint.getPosition());
                inFlightMatches.add(tileGraphic);
            }
        }
        scoringCalculator.addToScore(matches);
    }

    @Override
    public void dispose() {
        this.endGameSubscribers.clear();
    }

    @Override
    public void onMoveComplete() {
        movesCompleted++;
        movesToGo--;
        if (movesToGo == 0) {
            handleEndGame();
        }
    }

    private void handleEndGame() {
        if (scoringCalculator.getScore() < targetScore) {
            for (EndGameSubscriber endGameSubscriber : endGameSubscribers) {
                endGameSubscriber.notifyGameShouldEnd(new GameStats(movesCompleted, scoringCalculator.getScore()));
            }
        } else {
            recalculateTargetGoal();
        }
    }

    private void recalculateTargetGoal() {
        targetScore = scoringCalculator.getScore() * 2;
        // For now let's just always have 20 moves.
        movesToGo = 20;
    }

    public void addEndGameSubscriber(EndGameSubscriber endGameSubscriber) {
        this.endGameSubscribers.add(endGameSubscriber);
    }

    public void  removeEndGameSubscriber(EndGameSubscriber endGameSubscriber) {
        this.endGameSubscribers.remove(endGameSubscriber);
    }

}
