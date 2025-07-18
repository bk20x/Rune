package rune.editor.objects;

import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.utils.Pair;
import rune.editor.types.ItemTypes;
import rune.editor.types.Rarity;

import javax.swing.text.html.Option;
import java.io.FileNotFoundException;
import java.util.Optional;

import static rune.editor.data.GameData.setItemValues;



public class Item {

    public String name;
    public ItemTypes type;
    public Rarity rarity;
    public Texture texture;
    public float baseDamage, damage;
    public int amount = 1;
    public Pair<String,Float> effect;
    public Item(){}

    public Item(String name, ItemTypes type){
        this.name = name;
        this.type = type;
        setItemValues(this);
        this.texture = getTexture();
        if(type == ItemTypes.WEAPON){
            damage = baseDamage;
        }
    }
    public Item(String name, ItemTypes type, int amount){
        this.name = name;
        this.type = type;
        this.amount = amount;
        setItemValues(this);
        if(type == ItemTypes.WEAPON){
            damage = baseDamage;
        }
    }

    public Item(String name){
        this.name = name;
        setItemValues(this);
        if(type == ItemTypes.WEAPON){
            damage = baseDamage;
        }
    }
    public void setType(ItemTypes type){this.type = type;}
    public void setRarity(Rarity rarity){this.rarity = rarity;}
    public void setDamage(float damage){this.damage = damage;}
    public void setAmount(int amount){this.amount = amount;}
    public void setBaseDamage(float baseDamage){this.baseDamage = baseDamage;}
    public void setEffect(String effect, Float value){
        this.effect = new Pair<>(effect,value);
    }
    public static Item New(String name,ItemTypes type){
        return new Item(name,type);
    }

    public static Item New(String name){
        return new Item(name);
    }
    public static Item New(){
        return new Item();
    }


    private void setPotionEffect(){

    }


    public Texture getTexture(){
        try {
             return new Texture("items/"+name+".png");
        }
        catch (RuntimeException e){
            return new Texture("items/" + "generic" + type.toString().toLowerCase() +".png");
        }
    }
}
