package rune.editor.scene;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.github.javaparser.ast.type.VoidType;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.entity.Slime;
import rune.editor.system.EntitySystem;
import rune.editor.types.Rarity;

import java.sql.Struct;


public class Scene{

    public TiledMap map;
    public final OrthogonalTiledMapRenderer mapRenderer;
    public EntitySystem entitySystem;
    public int width, height;
    private boolean disposed = false;



    public Scene(){
        map = new TmxMapLoader().load("pmap/pmap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        entitySystem = new EntitySystem();
        width = map.getProperties().get("width",Integer.class)*32;
        height = map.getProperties().get("height",Integer.class)*32;


    }

    public Scene(String name){
        map = new TmxMapLoader().load( name + "/" + name + ".tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        entitySystem = new EntitySystem();
        width = map.getProperties().get("width",Integer.class)*32;
        height = map.getProperties().get("height",Integer.class)*32;
    }


    public void setView(OrthographicCamera camera){
        mapRenderer.setView(camera);
    }
    public void update(){

    }
    public void playerInteract(Player player){

    }

    public void draw(Renderer renderer, float dt){
        mapRenderer.render();
        entitySystem.draw(renderer, dt);
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
            if (entitySystem != null && entitySystem.entities != null) {
                for (Entity entity : entitySystem.entities.values()) {
                    if (entity != null) {
                        entity.dispose();
                    }
                }
                entitySystem.entities.clear();
            }
            disposed = true;
        }
    }



}
