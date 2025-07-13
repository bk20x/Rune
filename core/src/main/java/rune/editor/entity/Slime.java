package rune.editor.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.data.GameData;
import rune.editor.types.DIRECTION;
import rune.editor.types.EntityTypes;



public class Slime extends Entity implements MobIface {

    private final Animation<TextureRegion> north;
    private final Animation<TextureRegion> east;
    private final Animation<TextureRegion> west;
    private final Animation<TextureRegion> south;
    private Animation<TextureRegion> hurtAnim;
    private final TextureRegion[] regions;
    private final TextureRegion[] altRegions;

    private Texture shadow;
    public Slime(String color){
        this.name = color + "_slime";
        GameData.setMobValues(this);
        this.direction = DIRECTION.SOUTH;
        this.pos = new Vector2();
        this.bounds = new Rectangle(0,0,32,32);
        this.id = basenum++;
        this.type = EntityTypes.MOB;

        this.texture = new Texture("entities" + "/" + name + ".png");
        this.regions = TextureRegion.split(texture, 32, 32)[0];
        this.altRegions = TextureRegion.split(texture, 32, 32)[1];

        north = new Animation<TextureRegion>(0.30f, altRegions[3], altRegions[4], altRegions[5]);
        east = new Animation<TextureRegion>(0.30f, regions[3], regions[4], regions[5]);
        west = new Animation<TextureRegion>(0.30f, altRegions[0], altRegions[1], altRegions[2]);
        south = new Animation<TextureRegion>(0.30f, regions[0], regions[1], regions[2]);

        this.shadow = new Texture("shadow.png");
        this.range = new Circle(pos.x,pos.y,regions[0].getRegionWidth() * 2);
    }

    public static Slime Blue(){
        return new Slime("blue");
    }
    public static Slime Orange(){
        return new Slime("orange");
    }
    public static Slime Green(){
        return new Slime("green");
    }
    public static Slime Purple(){return new Slime("purple");}
    @Override
    public void draw(Renderer renderer, float dt){
        stateTime += Gdx.graphics.getDeltaTime(); if(stateTime > 10) stateTime = 0;
        renderer.sb.draw(shadow,pos.x + 5, pos.y - 6, 20, 14);
        renderer.sb.draw(mobAnim().getKeyFrame(stateTime, true), pos.x, pos.y);
    }

    public Animation<TextureRegion> mobAnim(){
        return switch (direction){
            case SOUTH -> south;
            case NORTH -> north;
            case WEST -> west;
            case EAST -> east;
        };
    }

    public void update(float dt){
        super.update(dt);
       if(moving){
            range.setPosition(pos.x,pos.y);
            bounds.setPosition(pos.x,pos.y);
       }
    }
    @Override
    public void setPos(float x ,float y){
        this.pos.set(x,y);
        bounds.setPosition(x,y);
        range.setPosition(x,y);
    }

    @Override
    public void follow(Entity mob, Player player, float dt){
        if(player.range.overlaps(mob.range)){
            mob.moving = true;
            if(player.pos.y > mob.pos.y){
                mob.pos.y += mob.speed * dt;
                mob.direction = player.direction;
                if(mob.bounds.overlaps(player.bounds)){
                    mob.pos.y -= mob.speed * dt;
                }
            }
            if(player.pos.x > mob.pos.x) {
                mob.pos.x += mob.speed * dt;
                mob.direction = DIRECTION.EAST;
                if(mob.bounds.overlaps(player.bounds)){
                    mob.pos.x -= mob.speed * dt;
                }
            }

            if(player.pos.y < mob.pos.y){
                mob.pos.y -= mob.speed * dt;
                mob.direction = DIRECTION.SOUTH;
                if(mob.bounds.overlaps(player.bounds)){
                    mob.pos.y += mob.speed * dt;
                }

                if(player.direction == DIRECTION.EAST) mob.direction = DIRECTION.EAST;

            }
            if(player.pos.x < mob.pos.x){
                mob.pos.x -= mob.speed * dt;
                mob.direction = DIRECTION.WEST;
                if(mob.bounds.overlaps(player.bounds)){
                    mob.pos.x += mob.speed * dt;
                }
                if(player.pos.y < mob.pos.y){
                    if(player.direction == DIRECTION.SOUTH) mob.direction = DIRECTION.SOUTH;
                }

                if(player.pos.y > mob.pos.y){
                    if(player.direction == DIRECTION.NORTH) mob.direction = DIRECTION.NORTH;
                }

            }
        }else {
            mob.moving = false;
        }
    }
    @Override
    public void followPlayer(Player player, float dt){
        follow(this, player, dt);
    }

    @Override
    public void fightPlayer(Player player){
        super.fightPlayer(player);
    }

    @Override
    public Slime clone() {
        return this;
    }
}
