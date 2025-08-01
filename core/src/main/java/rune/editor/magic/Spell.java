package rune.editor.magic;

import com.badlogic.gdx.math.Vector2;
import rune.editor.types.SpellTypes;

import static rune.editor.data.GameData.setSpellValues;

public class Spell {

    public String name;
    public float damage;
    public float travelSpeed;
    public float range;
    public Vector2 start, pos;
    public boolean isRanged;
    public SpellTypes type;

    public Spell(String name) {
        this.name = name;
        setSpellValues(this);
    }


    public boolean travelledPastRange(){
        return start.dst(pos) <= range;
    }

    public static Spell New(String name){
        return new Spell(name);
    }

}
