package space.peetseater.game.screens.transitions;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import space.peetseater.game.Match3Game;

import space.peetseater.game.screens.Scene;

import java.util.LinkedList;

import static space.peetseater.game.Constants.GAME_HEIGHT;
import static space.peetseater.game.Constants.GAME_WIDTH;

public class Transition extends ScreenAdapter implements TransitionStep {
    protected final Match3Game match3Game;
    public final TransitionType transitionType;

    public Scene from;
    public Scene to;
    public boolean started;
    public boolean finished;

    public final OrthographicCamera camera;
    protected final FitViewport viewport;
    protected LinkedList<TransitionStep> steps;

    public Transition(
            Match3Game match3Game,
            TransitionType transitionType,
            Scene from,
            Scene to
    ) {
        this.match3Game = match3Game;
        this.transitionType = transitionType;
        this.from = from;
        this.to = to;
        assert to != null;
        this.started = false;
        this.finished = false;

        camera = new OrthographicCamera();
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        camera.update();

        steps = new LinkedList<>();
        initializeSteps();
    }

    protected void initializeSteps() {
        steps.add(new FadeOutToBlack(this, 0.3f));
        steps.add(new FadeInFromBlack(this, 0.5f));
    }

    public void onStart() {
        // Ensure that the game knows to load the assets for the screen we're transitioning to
        match3Game.match3Assets.queue(to);
        if (!steps.isEmpty()) {
            steps.peek().onStart();
        }
        started = true;
    }

    public void update(float delta) {
        boolean canTransition = match3Game.match3Assets.assetManager.update(17);
        if (steps.isEmpty() && canTransition) {
            finished = true;
            return;
        }

        if (steps.isEmpty()) {
            return;
        }

        steps.peek().update(delta);
        if (steps.peek().isComplete()) {
            steps.peek().onEnd();
            steps.pop();
            if (!steps.isEmpty()) {
                steps.peek().onStart();
            }
        }
    }

    public void render(float delta) {
        if (steps.isEmpty()) {
            return;
        }
        steps.peek().render(delta);
    }

    @Override
    public boolean isComplete() {
        return finished;
    }

    public boolean isStarted() {
        return started;
    }

    public void onEnd() {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }
}
