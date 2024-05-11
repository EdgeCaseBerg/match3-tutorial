package space.peetseater.game.shared;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import space.peetseater.game.Match3Assets;

import java.util.ArrayList;
import java.util.List;

import static space.peetseater.game.Constants.TILE_UNIT_HEIGHT;
import static space.peetseater.game.Constants.TILE_UNIT_WIDTH;
import static space.peetseater.game.Match3Assets.SPARKLE_SPRITE_HEIGHT;
import static space.peetseater.game.Match3Assets.SPARKLE_SPRITE_WIDTH;

public class SparkleGraphic implements Disposable {
    private final List<Vector2> offsets;
    private final List<Float> stateTimes;
    private final Match3Assets match3Assets;
    private final Vector2 position;

    private boolean enabled;

    Animation<TextureRegion> sparkles;

    public SparkleGraphic(Vector2 position, Match3Assets match3Assets, float areaSize) {
        this.position = position;
        this.offsets = new ArrayList<Vector2>();
        this.stateTimes = new ArrayList<Float>();
        float offset = areaSize / 2;
        for (int i = 0; i < MathUtils.random(2, 4); i++) {
            this.offsets.add(new Vector2(
                    offset + MathUtils.random(-offset,offset), offset + MathUtils.random(-offset, offset)
            ));
            this.stateTimes.add(MathUtils.random());
        }
        this.match3Assets = match3Assets;
        this.sparkles = makeSparkles(match3Assets.getSparkleSheetTexture());
        this.enabled = false;
    }

    private Animation<TextureRegion> makeSparkles(Texture sparkleSheetTexture) {
        TextureRegion[][] regions = TextureRegion.split(sparkleSheetTexture, SPARKLE_SPRITE_WIDTH, SPARKLE_SPRITE_HEIGHT);
        TextureRegion[] keyFrames = new TextureRegion[regions.length * regions[0].length];
        for (int row = 0; row < regions.length; row++) {
            for (int column = 0; column < regions[row].length; column++) {
                keyFrames[row] = regions[row][column];
            }

        }
        return new Animation<TextureRegion>(0.12f, keyFrames);
    }

    public void render(float delta, SpriteBatch batch) {
        if (!enabled) {
            return;
        }
        for (int i = 0; i < stateTimes.size(); i++) {
            Vector2 position = this.position.cpy().add(offsets.get(i));
            Float stateTime = stateTimes.get(i);
            stateTime += delta;
            stateTimes.set(i, stateTime);
            TextureRegion keyframe = sparkles.getKeyFrame(stateTime, true);
            batch.draw(keyframe, position.x, position.y, TILE_UNIT_WIDTH / 4, TILE_UNIT_HEIGHT / 4);
        }
    }

    @Override
    public void dispose() {
        match3Assets.unloadSparkleSheetTexture();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
