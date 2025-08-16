package rune.editor.objects;

import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.utils.Pair;
import rune.editor.Globals;
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
    public Pair<String, Integer> effect;
    public int defense;
    public int duration;
    public int cost;

    public Item() {
    }

    public Item(String name, ItemTypes type) {
        this.name = name;
        this.type = type;
        this.amount = 1;
        setItemValues(this);
        this.texture = getTexture();
        if (type == ItemTypes.WEAPON) {
            damage = baseDamage;
        }
    }

    public Item(String name, ItemTypes type, int amount) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        setItemValues(this);
        if (type == ItemTypes.WEAPON) {
            damage = baseDamage;
        }
    }

    public Item(String name) {
        this.name = name;
        this.amount = 1;
        setItemValues(this);
        if (type == ItemTypes.WEAPON) {
            damage = baseDamage;
        }
    }

    public void setType(ItemTypes type) {
        this.type = type;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setEffect(String effect, Integer value) {
        this.effect = new Pair<>(effect, value);
    }

    public static Item New(String name, ItemTypes type) {
        return new Item(name, type);
    }

    public static Item New(String name) {
        return new Item(name);
    }

    public static Item New() {
        return new Item();
    }


    private void setPotionEffect() {

    }


    public Texture getTexture() {
        try {
            return new Texture(Globals.itemAssetPath + name + ".png");
        } catch (RuntimeException e) {
            return new Texture("shadow.png");
            //return new Texture("items/" + "generic" + type.toString().toLowerCase() +".png");
        }

    }
}
