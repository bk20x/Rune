package rune.editor.maps;

import net.dermetfan.gdx.physics.box2d.PositionController;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.objects.Chest;
import rune.editor.objects.Item;
import rune.editor.scene.Scene;
import rune.editor.types.Rarity;

import java.util.ArrayList;
import java.util.Vector;

public class DungeonMap extends Scene {


    private ArrayList<Chest> chests;

    public DungeonMap(String name) {
        super(name);
        this.chests = new ArrayList<>();

        setChests();
    }

    @Override
    public void draw(Renderer renderer, float dt){
        mapRenderer.render();
        entitySystem.draw(renderer,dt);
        for (Chest chest : chests) {
            chest.draw(renderer, dt);
        }
    }

    @Override
    public void playerInteract(Player player){
        for (Chest chest : chests) {
            chest.openChest(player);
        }
    }

    public void setChests(){
        for (int i = 0; i < map.getProperties().get("amount_of_chests", Integer.class); i++) {
            chests.add(new Chest());
        }
        for (int i = 0; i < chests.size(); i++) {
            var chestValues = map.getProperties().get("chest" + i, String.class).split(",");
            var chestPosition = chestValues[1].split(";");
            var rarity = Rarity.fromString(chestValues[0]);

            float chestX = Float.parseFloat(chestPosition[0]);
            float chestY = Float.parseFloat(chestPosition[1]);

            chests.get(i).setRarity(rarity);
            chests.get(i).setPos(chestX,chestY);
            chests.get(i).fillWithRandom();
        }
    }




}
