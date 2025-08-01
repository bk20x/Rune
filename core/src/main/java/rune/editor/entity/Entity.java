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
import rune.editor.types.DIRECTION;
import rune.editor.types.EntityTypes;
import rune.editor.types.Rarity;


import static rune.editor.data.GameData.setMobValues;


public class Entity implements MobIface,Cloneable {

    public String name;
    public int id;
    public EntityTypes type;
    public Vector2 pos;
    public Rarity rarity;
    public DIRECTION direction;
    protected static int basenum = 1;
    public Texture texture;
    public TextureRegion[] regions,altRegions;
    public float health;
    public float speed;
    public float damage;
    public Rectangle bounds;
    public Circle range;
    public boolean alive = true;
    public boolean moving = false;
    public boolean attacking = false;
    public boolean hurt = false;
    public boolean isUsed = false;
    public Animation<TextureRegion> animation;

    public Entity(int id){
        this.id = id;
    }
    public Entity(String name,EntityTypes type){
        this.name = name;
        this.id = basenum++;
        if(basenum > 5000) basenum = 1;
        this.type = type;
        this.pos = new Vector2(0,0);
        setTextures();
        bounds = new Rectangle(pos.x,pos.y,texture.getWidth(),texture.getHeight());

        if(type == EntityTypes.MOB){
            setMobValues(this);
            regions = TextureRegion.split(texture, 32, 32)[0];
            altRegions = TextureRegion.split(texture, 32, 32)[1];
            direction = DIRECTION.SOUTH;
        }
    }

    public Entity(){}

    public static Entity staticObj(String name){
        return new Entity(name,EntityTypes.STATIC_OBJ);
    }
    public static Entity animatedObj(String name){
        return new Entity(name,EntityTypes.ANIMATED_OBJ);
    }
    public static Entity mob(String name){
        return new Entity(name,EntityTypes.MOB);
    }


    public Animation<TextureRegion> getAnim(){
        return switch (direction){
            case NORTH -> {
                 yield new Animation<TextureRegion>(0.30f, altRegions[3], altRegions[4], altRegions[5]);
            }
            case EAST -> {
                 yield new Animation<TextureRegion>(0.30f, regions[3], regions[4], regions[5]);
            }
            case WEST -> {
                yield new Animation<TextureRegion>(0.30f, altRegions[0], altRegions[1], altRegions[2]);
            }
            case SOUTH ->{
               yield new Animation<TextureRegion>(0.30f, regions[0], regions[1], regions[2]);
            }
        };
    }

    public void draw(Renderer renderer, float x,float y){
        renderer.sb.draw(texture, x, y);
        if(type == EntityTypes.MOB){
            renderer.sb.draw(getAnim().getKeyFrame(stateTime, true), pos.x, pos.y);
        }
        if(type == EntityTypes.ANIMATED_OBJ){
            renderer.sb.draw(animation.getKeyFrame(stateTime, true), x, y);
        }
        if(type == EntityTypes.STATIC_OBJ){
            renderer.sb.draw(texture, x, y);
        }
    }
    float stateTime;




    public void draw(Renderer renderer,float dt){
        update(dt);

        stateTime += Gdx.graphics.getDeltaTime();
        if(stateTime > 3) stateTime = 0;

        if(type == EntityTypes.MOB){
            renderer.sb.draw(getAnim().getKeyFrame(stateTime, true), pos.x, pos.y);
        }
    }




    public void update(float dt){
        if(appliedDamage > 0){
            applyDamage();
        }
    }

    public void dispose(){
        if (texture != null) {
            texture.dispose();
            texture = null;
        }

        animation = null;
        regions = null;
        altRegions = null;
    }


    public void setSpeed(float speed){this.speed = speed;}
    public void setDirection(DIRECTION direction){this.direction = direction;}
    public void setTextures(){texture = new Texture("entities/"+name+".png");}


    public void setX(float x){
        pos.x = x;
    }
    public void setY(float y){
        pos.y = y;
    }

    public EntityTypes type(){
        return this.type;
    }
    public String name(){return  name;}

    public int id(){
        return  id;
    }
    public void setPos(float x,float y){
        pos.set(x,y);
        bounds.setPosition(x,y);
    }

    public void setHealth(float health){
        this.health = health;
    }
    public void setDamage(float damage){
        this.damage = damage;
    }
    public void setRarity(Rarity rarity){
        this.rarity = rarity;
    }

    @Override public void follow(Entity mob, Player player, float dt){

    }
    public boolean collided(Rectangle rec){
        return rec.overlaps(bounds) && alive;
    }

    boolean t;
    @Override
    public void attack(Entity mob, Player player){
        t = true;

    }
    public float appliedDamage;
    public void applyDamage(){
        health -= appliedDamage;
        appliedDamage = 0;
        if(health < 0) {
            health = 0;
        }
        if(health <= 0){
            alive = false;
            this.bounds.setSize(0,0);
            this.range.set(0,0,0);
        }
    }
    public void fightPlayer(Player player){
        attack(this,player);
    }
    public void followPlayer(Player player, float dt){
        follow(this,player,dt);
    }
    public void unwalkableObject(Player player){
        if(player.isMoving && player.bounds.overlaps(bounds)){
            switch (player.direction){
                case WEST -> player.pos.x += player.speed * Gdx.graphics.getDeltaTime();
                case EAST -> player.pos.x -= player.speed * Gdx.graphics.getDeltaTime();
                case NORTH -> player.pos.y -= player.speed * Gdx.graphics.getDeltaTime();
                case SOUTH -> player.pos.y += player.speed * Gdx.graphics.getDeltaTime();
            }
        }
    }
    @Override
    public Entity clone(){
        return this;
    }

}
