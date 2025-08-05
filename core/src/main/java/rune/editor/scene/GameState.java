package rune.editor.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.gui.Button;
import rune.editor.gui.GuiLayout;


public class GameState {



    public static String activeQuest;
    public static void setActiveQuest(String questName) {
        activeQuest = questName;
    }
    public Scene scene;
    public Player player;
    public final Renderer renderer;
    public boolean isSceneActive = true;
    public OrthographicCamera camera;
    public SceneTransitionManager transitionManager;

    public GuiLayout guiLayout;
    public Button button;
    public GameState(){
        renderer = new Renderer();
        guiLayout = new GuiLayout();
        transitionManager = new SceneTransitionManager();

        button = new Button(1,"def","def");

    }

    public void addEntity(Entity e){
        scene.entityManager.add(e);
    }
    public void addPlayer(Player player){
        this.player = player;
    }



    public synchronized void setScene(Scene scene) {
        isSceneActive = false;
        renderer.flush();
        if (player != null) {
            player.lastEntityKilled = null;
        }
        if (this.scene != null) {
            this.scene.dispose();
        }
        this.scene = scene;
        isSceneActive = true;
    }

    public void transitionToScene(Scene targetScene, String exitDirection) {
        if (!transitionManager.isTransitioning()) {
            transitionManager.startTransition(targetScene, exitDirection);
        }
    }


    public void setTint(Color color){
        renderer.sb.setColor(color);
        scene.mapRenderer.getBatch().setColor(color);
    }

    public void setView(OrthographicCamera camera){
        if (this.camera == null) {
            this.camera = camera;
        }
        scene.setView(this.camera);
        renderer.setView(this.camera);
    }


    public Vector2 getMousePos(){
        Vector3 screenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 unprojected = camera.unproject(screenCoords);
        return new Vector2(unprojected.x, unprojected.y);
    }

    public void run(Renderer renderer, float dt) {
        boolean transitionComplete = transitionManager.updateTransitionState(dt, player, this);


        renderer.start();
        if (isSceneActive && scene != null && player != null && camera != null) {
            scene.update();
            renderer.setView(camera);
            scene.setView(camera);



            scene.draw(renderer, dt);

            button.draw(renderer, camera.position.x - 200, camera.position.y - 100);
            button.setBounds(camera.position.x - 200, camera.position.y - 100, 100, 100);
            button.trigger(getMousePos());
            scene.playerInteract(player);
            if (player.isMelee) {
                scene.entityManager.combat(player);
            }
            if (transitionComplete) {
                player.draw(renderer, dt);
            }
            if (!transitionManager.isTransitioning()) {
                checkSceneTransitions();
            }
            for (int i : scene.entityManager.entities.keySet()) {
                scene.entityManager.entities.get(i).followPlayer(player, dt);
            }

        }


        transitionManager.renderTransitionEffect();
        renderer.stop();



    }


    private void checkSceneTransitions() {
        if (player == null || scene == null) return;


        if (player.pos.x < 64 && player.isMoving && player.direction == rune.editor.types.DIRECTION.WEST) {

            Scene newScene = new Scene();
            transitionToScene(newScene, "left");
        }
    }

    public static GameState New(){return new GameState();}
}
