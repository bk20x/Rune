package rune.editor.objects;

import com.badlogic.gdx.math.MathUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rune.editor.types.ItemTypes;
import rune.editor.types.Rarity;

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
        final int random = MathUtils.random(0,3);

        ItemTypes type = switch (random){
            case 0, 1 -> ItemTypes.WEAPON;
            case 2 ->  ItemTypes.ARMOR;
            case 3 ->  ItemTypes.POTION;
            default ->  ItemTypes.MISC;
        };

        System.out.println(type);

        for (JsonElement element: arrayFromFile("json/items.json")) {
            JsonObject itemData = element.getAsJsonObject();

            System.err.println(itemData.get("rarity").getAsString().equals(rarity.toString()) + " " + itemData.get("name").getAsString() + " " + rarity);

            if(itemData.get("rarity").getAsString().equals(rarity.toString()) && itemData.get("type").getAsString().equals(type.toString())){
                System.err.println("random got: " + itemData.get("name").getAsString());
                return Item.New(itemData.get("name").getAsString());
            }
        }
        return Items.Empty();
    }




}
