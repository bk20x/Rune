package rune.editor.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import rune.editor.Player;
import rune.editor.Renderer;


public class SceneTransitionManager {

    public enum TransitionState {
        NONE,
        FADE_OUT,
        FADE_IN
    }

    public enum ManipType{
        ZOOM_IN,
        ZOOM_OUT,
        ROTATE
    }

    public TransitionState currentState = TransitionState.NONE;
    public float alpha = 0f;
    public float fadeDuration = 0.60f;
    public float fadeTimer = 0f;
    public Scene targetScene;
    public String exitDirection;

    public SceneTransitionManager() {}

    public void startTransition(Scene targetScene, String exitDirection) {
        if (currentState == TransitionState.NONE) {
            this.targetScene = targetScene;
            this.exitDirection = exitDirection;
            currentState = TransitionState.FADE_OUT;
            fadeTimer = 0f;
        }
    }


    public boolean updateTransitionState(float dt, Player player, GameState gameState) {
        if (currentState == TransitionState.NONE) {
            return true;
        }
        fadeTimer += dt;
        if (currentState == TransitionState.FADE_OUT) {
            alpha = MathUtils.clamp(fadeTimer / fadeDuration, 0f, 1f);

            if (fadeTimer >= fadeDuration) {
                setActiveSceneToTarget(gameState,player);
                currentState = TransitionState.FADE_IN;
                fadeTimer = 0f;
            }
        }if (currentState == TransitionState.FADE_IN) {
            alpha = MathUtils.clamp(1f - (fadeTimer / fadeDuration), 0f, 1f);

            if (fadeTimer >= fadeDuration) {
                currentState = TransitionState.NONE;
                alpha = 0f;
                return true;
            }
        }
        return false;
    }
    private void setActiveSceneToTarget(GameState gameState, Player player) {


        gameState.setScene(targetScene);


        if (player != null) {
            switch (exitDirection) {
                case "left":
                    player.pos.x = targetScene.width - 96;
                    break;
                case "right":
                    player.pos.x = 96;
                    break;
                case "up":
                    player.pos.y = 96;
                    break;
                case "down":
                    player.pos.y = targetScene.height - 96;
                    break;

            }
                player.isMoving = false;
                if (player.bounds != null) {
                    player.bounds.setPosition(player.pos.x, player.pos.y);
                }

                if (player.range != null) {
                    player.range.setPosition(player.pos.x, player.pos.y);
                }




        }
    }


    public void beginManipulateCamera(OrthographicCamera camera, ManipType manipType) {
        ManipType manip = manipType;






    }

    public void renderTransitionEffect() {
        if (currentState != TransitionState.NONE) {
            Renderer renderer = new Renderer();
            Color fadeColor = new Color(0, 0, 0, alpha * 0.75f);


            renderer.start();



            renderer.fillScreen(fadeColor);

            renderer.stop();
            renderer.dispose();
            renderer = null;
        }
    }


    public boolean isTransitioning() {
        return currentState != TransitionState.NONE;
    }
}
