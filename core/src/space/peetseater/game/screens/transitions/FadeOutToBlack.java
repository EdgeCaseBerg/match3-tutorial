package space.peetseater.game.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import space.peetseater.game.TestTexture;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

class FadeOutToBlack implements TransitionStep {
    private final Transition transition;
    private float accum = 0f;
    public boolean done = false;
    private float minLength = 1f;
    private final Texture black;
    private Texture fromTexture;
    FrameBuffer frameBuffer;

    public FadeOutToBlack(Transition transition, float minLength) {
        this.transition = transition;
        this.minLength = minLength;
        // TODO: Move black to assets
        this.black = TestTexture.makeTexture(Color.BLACK);
    }

    public void update(float delta) {
        accum += delta;
        if (accum >= minLength) {
            done = transition.match3Game.match3Assets.getProgress() == 100;
        }
    }

    public void render(float delta) {
        int progress = transition.match3Game.match3Assets.getProgress();
        float alpha = Math.min(accum / minLength, progress / 100f);

        transition.camera.update();
        transition.match3Game.batch.setProjectionMatrix(transition.camera.combined);
        ScreenUtils.clear(Color.BLACK);

        transition.match3Game.batch.begin();
        if (fromTexture != null) {
            transition.match3Game.batch.draw(
                    fromTexture, 0f, 0f, GAME_WIDTH, GAME_HEIGHT, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true
            );
            Color batchColor = transition.match3Game.batch.getColor().cpy();
            transition.match3Game.batch.setColor(
                    batchColor.r,
                    batchColor.g,
                    batchColor.b,
                    MathUtils.lerp(0, 1, alpha)
            );
            transition.match3Game.batch.draw(
                    black,
                    0, 0,
                    GAME_WIDTH,
                    GAME_HEIGHT
            );
            transition.match3Game.batch.setColor(batchColor.r, batchColor.g, batchColor.b, 1);
        }
        transition.match3Game.font.draw(
                transition.match3Game.batch,
                "Loading " + (int) (alpha * 100) + "%",
                GAME_WIDTH - 4, 1
        );
        transition.match3Game.batch.end();
    }

    public boolean isComplete() {
        return done;
    }

    public void onStart(){
        if (transition.from != null) {
            // Save last rendered scene to texture
            // Otherwise I suppose we're just coming from black.
            frameBuffer = new FrameBuffer(
                    Pixmap.Format.RGBA8888,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight(),
                    false
            );
            transition.match3Game.batch.flush();
            frameBuffer.begin();
            transition.from.render(0);
            frameBuffer.end();
            fromTexture = frameBuffer.getColorBufferTexture();
        }
    }

    public void onEnd() {
        black.dispose();
        if (fromTexture != null) {
            fromTexture.dispose();
            frameBuffer.dispose();
            fromTexture = null;
        }

    }
}
