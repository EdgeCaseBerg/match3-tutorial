package space.peetseater.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import space.peetseater.game.grid.BoardGraphic;
import space.peetseater.game.grid.GridSpace;
import space.peetseater.game.grid.match.Match;
import space.peetseater.game.grid.match.MatchSubscriber;
import space.peetseater.game.shared.MovablePoint;
import space.peetseater.game.tile.TileGraphic;
import space.peetseater.game.tile.TileType;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ScoreGraphic implements MatchSubscriber<TileType> {

    private final ScoringCalculator scoringCalculator;
    private final MovablePoint movablePoint;
    private final BoardGraphic boardGraphic;
    private final Texture texture;

    private LinkedList<TileGraphic> inFlightMatches;
    private Match3Assets match3Assets;

    public ScoreGraphic(Vector2 position, BoardGraphic boardGraphic, Match3Assets match3Assets) {
        this.scoringCalculator = new ScoringCalculator();
        inFlightMatches = new LinkedList<>();
        this.movablePoint = new MovablePoint(position);
        // TODO I don't like that I take this in to calc where to start
        // the tile graphics in flight path to the board. We can revisit
        // this after making some form of class for the match particles
        this.boardGraphic = boardGraphic;
        this.texture = TestTexture.makeTexture(new Color(1, 1, 1, 0.5f));
        this.match3Assets = match3Assets;
    }
    void render(float delta, SpriteBatch spriteBatch, BitmapFont bitmapFont) {
        float cornerX = movablePoint.getPosition().x;
        float cornerY = movablePoint.getPosition().y;
        spriteBatch.draw(
                texture,
                cornerX,
                cornerY,
                Constants.SCORE_UNIT_WIDTH,
                Constants.SCORE_UNIT_HEIGHT
        );
        bitmapFont.draw(
                spriteBatch,
                "Score: " + scoringCalculator.getScore() + "\n" + "Multiplier: x" + scoringCalculator.getMultiplier(),
                cornerX + Constants.BOARD_UNIT_GUTTER,
                cornerY + Constants.SCORE_UNIT_HEIGHT - Constants.BOARD_UNIT_GUTTER
        );
        for (TileGraphic tileGraphic : inFlightMatches) {
            tileGraphic.render(delta, spriteBatch);
        }
    }

    void update(float delta) {
        ListIterator<TileGraphic> iter = inFlightMatches.listIterator();
        while (iter.hasNext()) {
            TileGraphic tileGraphic = iter.next();
            if (tileGraphic.getMovablePoint().isAtDestination()) {
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
                float ty = boardGraphic.screenYFromGridRow(space.getRow());
                float tx = boardGraphic.screenXFromGridColumn(space.getColumn());
                TileGraphic tileGraphic = new TileGraphic(new Vector2(tx, ty), match.getValues().get(0), match3Assets);
                tileGraphic.getMovablePoint().setDestination(movablePoint.getPosition());
                inFlightMatches.add(tileGraphic);
            }
        }
        scoringCalculator.addToScore(matches);
    }
}
