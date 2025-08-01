package rune.editor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import rune.editor.Statics;
import rune.editor.Game;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.types.EntityTypes;

import java.util.Objects;
import java.util.Vector;

public class Chest extends Entity {

    public Vector<Item> items;
    public int width, height;

    private float stateTime;
    private Rectangle interactZone;

    public Chest() {
        this.type = EntityTypes.ANIMATED_OBJ;
        this.items = new Vector<>();
        this.pos = new Vector2();

        this.texture = new Texture(Gdx.files.internal(Statics.objectPath + "chest.png"));
        this.regions = TextureRegion.split(texture, 18,14)[0];

        height = regions[0].getRegionHeight()/2;
        width = regions[0].getRegionWidth()/2;
        this.animation = new Animation<>(0.10f,regions[0],regions[1]);
        this.bounds = new Rectangle(pos.x - width,pos.y - height,width,height);
        this.interactZone = new Rectangle(pos.x - width*2,pos.y - height*2,width*2,height*2);
    }

    public void openChest(Player player){
        unwalkableObject(player);
        if(player.bounds.overlaps(this.interactZone)){
            if(Gdx.input.isKeyJustPressed(Game.keyBinds.get("INTERACT")) && !isUsed){
                player.inventory.addItems(items);
                for (Item item : items) {
                    if(item.name != null)
                        System.out.println("Got: " + item.name);
                }
                items.clear();
                isUsed = true;
            }
            else if(Gdx.input.isKeyJustPressed(Game.keyBinds.get("INTERACT")) && isUsed){
                isUsed = false;
            }
        }
    }

    @Override
    public void draw(Renderer renderer, float dt){
        if(isUsed){
            if(stateTime < 0.25f){
                stateTime += Gdx.graphics.getDeltaTime();
            }
        }
        else {
            if(stateTime > 0){
                stateTime -= Gdx.graphics.getDeltaTime();
            }
        }
        renderer.sb.draw(animation.getKeyFrame(stateTime,false),pos.x,pos.y);
    }


    public void fillWithRandom(){
        if(items.isEmpty()) {
            for (int i = 0; i < MathUtils.random(1, 5); i++) {
                var randomItem = Items.Random(this.rarity);
                if (randomItem != null) {
                    items.add(randomItem);
                }
            }
        }
        items.removeIf(Objects::isNull);
    }

    @Override
    public void setPos(float x, float y) {
        pos.set(x, y);
        bounds.set(x + width/2,y + height + 2,width,height);
        interactZone.set(x - width,y ,width*4,height*4);
    }
}
