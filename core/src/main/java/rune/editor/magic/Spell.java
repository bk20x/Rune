package rune.editor.magic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import rune.editor.Globals;
import rune.editor.Renderer;
import rune.editor.types.SpellTypes;

import static rune.editor.data.GameData.setSpellValues;

public class Spell {

    public final String name;
    public float damage;
    public float travelSpeed;
    public float range;
    public Vector2 start, pos, vel;
    public boolean isRanged, isAlive;
    public SpellTypes type;
    private Texture texture;


    public Spell(String name) {
        isAlive = true;
        this.name = name;
        setSpellValues(this);
    }

    public boolean loadTexture() {
        try {
            this.texture = new Texture(Globals.spellAssetPath + name + ".png");
            return true;
        }catch (Exception e) {
            System.err.println("Some spell, " + name + ", probably doesn't have a texture yet or its in the wrong place.");
            return false;
        }
    }

    public boolean travelledPastRange(){
        return start.dst(pos) <= range;
    }

    public void update(float dt) {
        pos.x += dt * travelSpeed;
    }

    public void initializeTravel(Vector2 src){
        if(start == null) {
            start = new Vector2(src.x, src.y);
            pos = new Vector2(src.x, src.y);
        }
    }

    public void draw(Renderer renderer, float dt) {
        if(isAlive) {
            final boolean isLoaded = loadTexture();

            if (!travelledPastRange() && isLoaded) {
                update(dt);
                renderer.sb.draw(texture, pos.x, pos.y);
            } else {
                dispose();
                isAlive = false;
            }
        }
    }


    public void dispose() {
        if(texture != null) {
            texture.dispose();
        }
    }


    public static Spell New(String name){
        return new Spell(name);
    }

}
