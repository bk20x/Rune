package rune.editor.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.jetbrains.annotations.NotNull;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.maps.DungeonMap;




public class GameState {



    public static String activeQuest;
    public static void setActiveQuest(String questName) {
        activeQuest = questName;
    }


    public Scene scene;
    private Player player;
    private final Renderer renderer;
    public boolean isSceneActive = true;


    private DungeonMap dmap;
    public GameState(){
        renderer = new Renderer();




    }

    public void addEntity(Entity e){
        scene.entitySystem.add(e);
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


    public void setTint(Color color){
        renderer.sb.setColor(color);
        scene.mapRenderer.getBatch().setColor(color);
    }
    public void setView(OrthographicCamera camera){
        scene.setView(camera);
    }

    public void run(@NotNull Renderer renderer, float dt){
        renderer.start();
        if(isSceneActive){
            scene.update();
            scene.playerInteract(player);

            if(player.isMelee) {
                scene.entitySystem.battle(player);
            }

            scene.draw(renderer,dt);
            player.draw(renderer,dt);
            for (int i : scene.entitySystem.entities.keySet()) {
                scene.entitySystem.entities.get(i).followPlayer(player,dt);
            }
        }


        renderer.stop();
    }

    public static GameState New(){return new GameState();}
}
