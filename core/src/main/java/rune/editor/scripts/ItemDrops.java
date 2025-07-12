package rune.editor.scripts;

import com.badlogic.gdx.math.MathUtils;
import rune.editor.entity.Entity;
import rune.editor.objects.Item;
import rune.editor.objects.Items;

public class ItemDrops {


    public Item[] drop(Entity e){
        Item[] items = null;
        switch (e.rarity){
            case COMMON -> {
                items = new Item[MathUtils.random(1,4)];
                for (int i = 0; i < items.length - 1; i++){
                    items[i] = Items.random(e.rarity);
                }
            }
            case UNCOMMON -> {
                items = new Item[MathUtils.random(2,5)];
                for (int i = 0; i < items.length - 1; i++){
                    items[i] = Items.random(e.rarity);
                }
            }
            case RARE -> {
                items = new Item[MathUtils.random(3,7)];
                for (int i = 0; i < items.length - 1; i++){
                    items[i] = Items.random(e.rarity);
                }
            }
            case EPIC -> {
                items = new Item[MathUtils.random(3,9)];
                for (int i = 0; i < items.length - 1; i++){
                    items[i] = Items.random(e.rarity);
                }
            }
        }
        return items;
    }


}
