package rune.editor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import rune.editor.State;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.scene.Scene;
import rune.editor.types.EntityTypes;

import java.util.Vector;

public class Chest extends Entity {

    private Vector<Item> items;
    private Animation<TextureRegion> animation;
    private boolean opened = false;
    private float stateTime;

    public AnimatedTiledMapTile chestTile;

    public Chest(Scene scene) {
        this.type = EntityTypes.ANIMATED_OBJ;
        this.rarity = scene.rarity;

        this.items = new Vector<>();
        for (int i = 0; i < MathUtils.random(2,7); i++){
            items.add(Items.Random(this.rarity));
        }
        this.bounds = new Rectangle(0,0,32,32);

    }

    public void openChest(Player player){
        if(player.bounds.overlaps(this.bounds)){
            if(Gdx.input.isKeyPressed(State.keyBinds.get("INTERACT"))){
                player.inventory.addItems(items);
                items.clear();
                opened = true;
            }
        }
    }

    public void render(Player player, Renderer renderer){
        openChest(player);
        if(opened){
            if(stateTime < 3f){
                stateTime += Gdx.graphics.getDeltaTime();
            }
        }

        renderer.sb.draw(animation.getKeyFrame(stateTime,false),bounds.x,bounds.y);
    }

}
