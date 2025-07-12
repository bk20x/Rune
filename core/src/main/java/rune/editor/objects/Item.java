package rune.editor.objects;

import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.utils.Pair;
import org.luaj.vm2.ast.Str;
import rune.editor.data.GameData;
import rune.editor.types.ItemTypes;
import rune.editor.types.Rarity;

import static rune.editor.data.GameData.setItemValues;



public class Item {

    public String name;
    public ItemTypes type;
    public Rarity rarity;
    public Texture texture;
    public float baseDamage, damage;
    public int amount = 1;
    public Pair<String,Float> effect;


    public Item(String name, ItemTypes type){
        this.name = name;
        this.type = type;
        //this.texture = setTextures();
        setItemValues(this);
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



    private void setPotionEffect(){

    }


    public Texture setTextures(){return new Texture("items/"+name+".png");}
}
