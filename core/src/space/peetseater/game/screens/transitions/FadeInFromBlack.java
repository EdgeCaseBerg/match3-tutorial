package space.peetseater.game.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import space.peetseater.game.TestTexture;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

class FadeInFromBlack implements TransitionStep {
    private final Transition transition;
    private float accum = 0f;
    public boolean done = false;
    private float minLength = 1f;
    private Texture black;

    public FadeInFromBlack(Transition transition, float minLength) {
        this.transition = transition;
        this.minLength = minLength;
        this.black = TestTexture.makeTexture(Color.BLACK);
    }

    @Override
    public void update(float delta) {
        accum += delta;
        if (accum > minLength) {
            done = true;
        }
    }

    @Override
    public boolean isComplete() {
        return done;
    }

    @Override
    public void render(float delta) {
        transition.camera.update();
        transition.match3Game.batch.setProjectionMatrix(transition.camera.combined);
        ScreenUtils.clear(Color.BLACK);
        // Render the screen we're going to go to underneath our transition piece.
        transition.to.render(delta);
        transition.match3Game.batch.begin();
        float lerped = MathUtils.lerp(0, 1, accum / minLength);
        Color batchColor = transition.match3Game.batch.getColor();
        transition.match3Game.batch.setColor(
                batchColor.r,
                batchColor.g,
                batchColor.b,
                1 - lerped
        );
        transition.match3Game.batch.draw(
                black,
                0, 0,
                GAME_WIDTH,
                GAME_HEIGHT
        );
        Color fontColor = transition.match3Game.font.getColor();
        transition.match3Game.font.getColor().set(fontColor.r, fontColor.g, fontColor.b, 1 - lerped);
        transition.match3Game.font.draw(
                transition.match3Game.batch,
                "Loading " + transition.match3Game.match3Assets.getProgress() + "%",
                GAME_WIDTH - 4, 1
        );
        transition.match3Game.font.getColor().set(fontColor.r, fontColor.g, fontColor.b, 1);
        transition.match3Game.batch.setColor(batchColor.r, batchColor.g, batchColor.b, 1);
        transition.match3Game.batch.end();
    }

    @Override
    public void onStart() {
        if (!transition.match3Game.match3Assets.assetManager.update(1)) {
            transition.match3Game.match3Assets.blockingLoad();
        }
        transition.to.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void onEnd() {
        black.dispose();
    }
}
