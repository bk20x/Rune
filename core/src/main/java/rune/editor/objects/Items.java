package rune.editor.objects;

import com.badlogic.gdx.math.MathUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rune.editor.types.ItemTypes;
import rune.editor.types.Rarity;

import static rune.editor.data.GameData.arrayFromFile;

public class Items {







    public static Item weapon(String name){return new Item(name, ItemTypes.WEAPON);}
    public static Item armor(String name){return new Item(name, ItemTypes.ARMOR);}
    public static Item misc(String name){return new Item(name, ItemTypes.MISC);}
    public static Item potion(String name,int amount){return new Item(name, ItemTypes.POTION,amount);}


    public static Item New(String name, ItemTypes type){return new Item(name,type);}

    public static Item random(Rarity rarity){
        final int rand = MathUtils.random(0,3);
        ItemTypes type = null;
        switch (rand){
            case 1 -> type = ItemTypes.MISC;
            case 2 -> type = ItemTypes.ARMOR;
            case 3 -> type = ItemTypes.WEAPON;
        }
        for (JsonElement element: arrayFromFile("json/items.json")) {
            JsonObject itemData = element.getAsJsonObject();
            if(itemData.get("rarity").getAsString().equals(rarity.toString().toLowerCase())){
                return Item.New(itemData.get("name").getAsString(),type);
            }
        }
        return Items.random(rarity);
    }




}
