package rune.editor.scene;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.system.EntityManager;

import static rune.editor.data.GameData.setSceneValues;


public class Scene{

    public TiledMap map;
    public final OrthogonalTiledMapRenderer mapRenderer;
    public EntityManager entityManager;
    public int width, height;
    private boolean disposed = false;
    public String name;


    public Scene(){
        map = new TmxMapLoader().load("pmap/pmap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        entityManager = new EntityManager();
        width = map.getProperties().get("width",Integer.class)*32;
        height = map.getProperties().get("height",Integer.class)*32;
    }

    public Scene(String name){
        this.name = name;
        setSceneValues(this);
        map = new TmxMapLoader().load( "maps/" + name + "/" + name + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        entityManager = new EntityManager();
        width = map.getProperties().get("width",Integer.class)*32;
        height = map.getProperties().get("height",Integer.class)*32;
    }


    public void setView(OrthographicCamera camera){
        mapRenderer.setView(camera);
    }
    public void update(){

    }


    public void blockWorldEdges(Player player){
        if(player.pos.x > width - 84 && player.isMoving){
            player.pos.x -= player.speed * Gdx.graphics.getDeltaTime();
        }
        if(player.pos.x < 64 && player.isMoving){
            player.pos.x += player.speed * Gdx.graphics.getDeltaTime();
        }
        if(player.pos.y > height - 72 && player.isMoving){
            player.pos.y -= player.speed * Gdx.graphics.getDeltaTime();
        }
        if(player.pos.y < 64 && player.isMoving){
            player.pos.y += player.speed * Gdx.graphics.getDeltaTime();
        }

    }
    public void playerInteract(Player player){
        blockWorldEdges(player);


    }

    public void draw(Renderer renderer, float dt){
        mapRenderer.render();
        entityManager.draw(renderer, dt);
    }

    public static Scene New(){return new Scene();}
    public static Scene New(String name){return new Scene(name);}


    public void dispose() {
        if (!disposed) {
            if (map != null) {
                map.dispose();
                map = null;
            }
            if (mapRenderer != null) {
                mapRenderer.dispose();
            }
            if (entityManager != null && entityManager.entities != null) {
                for (Entity entity : entityManager.entities.values()) {
                    if (entity != null) {
                        entity.dispose();
                    }
                }
                entityManager.entities.clear();
            }
            disposed = true;
        }
    }



}
