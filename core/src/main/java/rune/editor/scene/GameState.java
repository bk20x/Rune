package rune.editor.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.data.GameData;
import rune.editor.entity.Entity;


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

    public GameState(){
        renderer = new Renderer();
        transitionManager = new SceneTransitionManager();
    }

    public void addEntity(Entity e){
        scene.entityManager.add(e);
    }
    public void addPlayer(Player player){
        this.player = player;
    }



    public synchronized void setScene(Scene scene) {
        renderer.flush();
        if (player != null) {
            player.lastEntityKilled = null;
        }
        isSceneActive = false;

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

    public void run(Renderer renderer, float dt){
        boolean transitionComplete = transitionManager.updateTransitionState(dt,player,this);


        if (camera != null) {
            renderer.setView(camera);
            this.renderer.setView(camera);
            if (scene != null) {
                scene.setView(camera);
            }
        }

        renderer.start();
        if(isSceneActive){
            scene.update();
            scene.draw(renderer,dt);
            if(player != null) {
                scene.playerInteract(player);
                if (player.isMelee) {
                    scene.entityManager.combat(player);
                }
                if(transitionComplete){
                    player.draw(renderer, dt);
                }
                if(Gdx.input.isKeyPressed(Input.Keys.Y)){
                    GameData.writeSaveFile(player);

                }
                if (!transitionManager.isTransitioning()) {
                    checkSceneTransitions();
                }
                for (int i : scene.entityManager.entities.keySet()) {
                    scene.entityManager.entities.get(i).followPlayer(player, dt);
                }
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
