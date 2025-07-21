package rune.editor.objects;

import com.badlogic.gdx.math.MathUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rune.editor.types.ItemTypes;
import rune.editor.types.Rarity;

import java.util.ArrayList;
import java.util.List;

import static rune.editor.data.GameData.arrayFromFile;

public class Items {







    public static Item Weapon(String name){return new Item(name, ItemTypes.WEAPON);}
    public static Item Armor(String name){return new Item(name, ItemTypes.ARMOR);}
    public static Item Misc(String name){return new Item(name, ItemTypes.MISC);}
    public static Item Potion(String name,int amount){return new Item(name, ItemTypes.POTION,amount);}


    public static Item New(String name, ItemTypes type){return new Item(name,type);}

    public static Item New(String name){return new Item(name);}
    public static Item Empty(){
        return new Item();
    }


    public static Item Random(Rarity rarity){
        final int random = MathUtils.random(1,3);

        ItemTypes type = switch (random){
            case 1 -> ItemTypes.WEAPON;
            case 2 ->  ItemTypes.ARMOR;
            case 3 ->  ItemTypes.POTION;
            default ->  ItemTypes.MISC;
        };


        List<String> matchingItems = new ArrayList<>();
        for (JsonElement element: arrayFromFile("json/items.json")) {
            JsonObject itemData = element.getAsJsonObject();
            if(Rarity.valueOf(itemData.get("rarity").getAsString()).equals(rarity) && itemData.get("type").getAsString().equals(type.toString())){
                matchingItems.add(itemData.get("name").getAsString());
            }
        }


        if (!matchingItems.isEmpty()) {
            int randomItemIdx = MathUtils.random(0, matchingItems.size() - 1);
            String selectedItem = matchingItems.get(randomItemIdx);
            return Item.New(selectedItem);
        }

        return Items.Random(rarity);
    }




}
