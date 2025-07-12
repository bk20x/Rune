package rune.editor.maps;

import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.objects.Chest;
import rune.editor.scene.Scene;
import rune.editor.types.Rarity;

import java.util.ArrayList;
import java.util.Vector;

public class DungeonMap extends Scene {


    private ArrayList<Chest> chests;

    public DungeonMap(String name) {
        super(name);


        this.chests = new ArrayList<>();
        this.rarity = Rarity.fromString(map.getProperties().get("rarity",String.class));

        for (int i = 0; i < map.getProperties().get("amount_of_chests", Integer.class); i++) {
            chests.add(new Chest(this));
        }
        for (int i = 0; i < chests.size(); i++) {
            chests.get(i).setPos(map.getProperties().get("chest" + i + "x", Float.class), map.getProperties().get("chest" + i + "y", Float.class));
        }

    }



    @Override
    public void draw(Renderer renderer, float dt){
        mapRenderer.render();
        entitySystem.draw(renderer,dt);
        for (Chest chest : chests) {
            chest.draw(renderer, dt);
        }
    }




}
