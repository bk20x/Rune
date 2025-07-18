package rune.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import rune.editor.entity.Entity;
import rune.editor.objects.Item;
import rune.editor.quest.Quest;
import rune.editor.objects.Items;
import rune.editor.system.Inventory;
import rune.editor.types.DIRECTION;


import java.util.ArrayList;
import java.util.HashMap;

import static rune.editor.State.keyBinds;
import static rune.editor.data.GameData.loadPlayerSaveFile;
import static rune.editor.types.DIRECTION.EAST;
import static rune.editor.types.DIRECTION.WEST;

public class Player {


    private final Texture textureSheet;
    private final Texture meleeSheet;
    private final Texture shadow;
    public final HashMap<String,Integer> attributeLevels = new HashMap<>();
    {
        attributeLevels.put("strength", 10);
        attributeLevels.put("intelligence", 10);
        attributeLevels.put("charisma", 10);
        attributeLevels.put("luck",10);
    }
    private TextureRegion[] textureFrames;
    public Vector2 pos;
    public DIRECTION direction;
    public float speed = 100f;
    public float experience;
    private float delta;
    public float health = 100f;
    public float maxHealth = 100f;
    public boolean isMoving, isMelee,isAlive = true,isWeaponEquipped;
    public Rectangle bounds = new Rectangle(0,0,32,32);
    public final Inventory inventory = new Inventory();
    public Circle range;

    public ArrayList<Quest> quests;

    public Quest activeQuest;
    public Entity killed;

    public String name;
    private final ArrayList<Integer> hitEntities = new ArrayList<>();
    public Item currentWeapon;

    public Player(){
        textureSheet = new Texture("player.png");
        meleeSheet = new Texture("heromelee.png");
        shadow = new Texture("shadow.png");

        textureFrames = TextureRegion.split(textureSheet, 32, 32)[0];
        pos = new Vector2(0,0);

        direction = DIRECTION.SOUTH;
        inventory.addItems(Items.Weapon("drained_hero_sword"));

        quests = new ArrayList<>();
        range = new Circle(pos.x,pos.y,32);


        loadPlayerSaveFile(this);


    }

    private void input(float dt){
        isMoving = false;

        if (Gdx.input.isKeyPressed(keyBinds.get("UP")) && !isMelee){
            pos.y += speed * dt;
            direction = DIRECTION.NORTH;
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(keyBinds.get("DOWN")) && !isMelee){
            pos.y -= speed * dt;
            direction = DIRECTION.SOUTH;
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(keyBinds.get("LEFT")) && !isMelee){
            pos.x -= speed * dt;
            direction = DIRECTION.WEST;
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(keyBinds.get("RIGHT")) && !isMelee){
            pos.x += speed * dt;
            direction = EAST;
            isMoving = true;
            System.out.println(toJson().getAsJsonObject());
        }

        if(Gdx.input.isKeyJustPressed(keyBinds.get("MELEE")) && isWeaponEquipped && !isMelee){
            isMelee = true;
            delta = 0;

        }

        if(Gdx.input.isKeyPressed(Input.Keys.B)){
            equipWeapon("drained_hero_sword");

        }


        if(Gdx.input.isKeyPressed(Input.Keys.F)){
            unequipWeapon();
        }

    }

    public void equipWeapon(String weaponName){
        if(this.inventory.getItem(weaponName) != null){
            this.currentWeapon = inventory.getItem(weaponName);
            isWeaponEquipped = true;
            System.err.println(currentWeapon.name);
            System.err.println(currentWeapon.damage);
            System.err.println(currentWeapon.rarity);
        }
    }

    public void unequipWeapon(){
        this.currentWeapon = null;
        isWeaponEquipped = false;
    }
    public Rectangle wepRec = new Rectangle(0,0,32,32);

    public boolean hitMob(Entity mob){
        wepRec.set(0,0,0,0);
        if(hitEntities.contains(mob.id())) {
            return false;
        }

        if(isWeaponEquipped && isMelee) {
            switch (direction) {
                case NORTH -> {
                    wepRec.set(getX(), getY() + 16, 32,38);
                    if(wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0,0,0,0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
                case WEST -> {
                    wepRec.set(getX() - 16, getY(),  38,32);
                    if(wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0,0,0,0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
                case EAST -> {
                    wepRec.set(getX() + 16 , getY(), 38,32);
                    if(wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0,0,0,0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
                case SOUTH -> {
                    wepRec.set(getX(), getY() - 16, 32,38);
                    if(wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0,0,0,0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
            }
        }
        wepRec.set(0,0,0,0);
        return false;
    }
    public void draw(Renderer renderer,float dt){
        delta += Gdx.graphics.getDeltaTime();  if(delta > 10) delta = 0;
        update(dt);
        if(isMelee){
            final float shadowXDisplaced = pos.x + 16f;
            renderer.sb.draw(shadow, shadowXDisplaced, pos.y, shadow.getWidth() - 1,shadow.getHeight());
            renderer.sb.draw(getMeleeAnim(delta).getKeyFrame(delta, isMelee), pos.x, pos.y);

            if(getMeleeAnim(delta).isAnimationFinished(delta)){
                isMelee = false;
                hitEntities.clear();
            }

        }else {
            final float shadowX = pos.x + 8.8f;
            final float shadowY = pos.y - 4;
            renderer.sb.draw(shadow, shadowX, shadowY,shadow.getWidth() - 1 ,shadow.getHeight());
            renderer.sb.draw(getAnim(delta).getKeyFrame(delta, isMoving), pos.x, pos.y);
        }

    }

    public void update(float dt){
        input(dt);
        if(isMoving){
            bounds.setPosition(pos.x,pos.y);
            range.setPosition(pos.x,pos.y);
        }
        if(activeQuest != null) {
            activeQuest.act(this);
        }
        quests.removeIf(quest -> quest.complete);

        if(!isAlive){die();}
    }





    private Animation<TextureRegion> getMeleeAnim(float delta){
        textureFrames = TextureRegion.split(meleeSheet, 48, 52)[0];
        return switch (direction){
            case SOUTH ->  new Animation<>(0.10f, textureFrames[2]);
            case NORTH -> new Animation<>(0.10f, textureFrames[3],textureFrames[4],textureFrames[3]);
            case WEST -> new Animation<>(0.10f, textureFrames[0],textureFrames[5]);
            case EAST -> new Animation<>(0.10f, textureFrames[1],textureFrames[6]);
        };
    }



    public Animation<TextureRegion> getAnim(float delta){
        if(direction == EAST || direction == WEST){
            textureFrames = TextureRegion.split(textureSheet, 32, 32)[1];
        }
        else if(direction == DIRECTION.NORTH || direction == DIRECTION.SOUTH){
            textureFrames = TextureRegion.split(textureSheet, 32, 32)[0];
        }
        return switch (direction){
            case SOUTH -> new Animation<>(0.25f, textureFrames[2],textureFrames[1],textureFrames[0]);
            case NORTH -> new Animation<>(0.25f, textureFrames[5],textureFrames[4],textureFrames[3]);
            case WEST -> new Animation<>(0.25f, textureFrames[5],textureFrames[4],textureFrames[5]);
            case EAST -> new Animation<>(0.25f, textureFrames[2],textureFrames[3],textureFrames[2]);
        };
    }
    public void completeQuest(){
        activeQuest.complete = true;
        activeQuest = null;
        State.activeQuest = null;
    }
    public void addQuest(Quest quest){
        quests.add(quest);
    }
    public void setActiveQuest(Quest quest){
        activeQuest = quest;
        State.activeQuest = activeQuest.name;
    }

    private void die(){
        isMelee = false;
        isMoving = false;
        hitEntities.clear();

        if(experience > 0) {
            experience = Math.max(0, experience - 50);
        }
    }

    public static Player New(){
        return new Player();
    }
    public void setSpeed(float speed){this.speed = speed;}
    public float getX(){return pos.x;}
    public float getY(){return pos.y;}

    public boolean takeDamage(float damage) {
        health -= damage;

        if(health <= 0) {
            health = 0;
            isAlive = false;
        }

        return isAlive;
    }
    public void addItem(Item i){
        inventory.addItems(i);

    }

    public void heal(float amount) {
        health = Math.min(health + amount, maxHealth);

    }



    public JsonObject toJson(){
        try {
            JsonObject json = new JsonObject();
            JsonObject skills = new JsonObject();
            skills.addProperty("strength", attributeLevels.get("strength"));
            skills.addProperty("intelligence", attributeLevels.get("intelligence"));
            skills.addProperty("charisma", attributeLevels.get("charisma"));
            skills.addProperty("luck", attributeLevels.get("luck"));
            json.addProperty("name", name);
            json.addProperty("posX", pos.x);
            json.addProperty("posY", pos.y);
            json.addProperty("health", health);
            json.addProperty("experience", experience);
            if (this.currentWeapon != null) {
                json.addProperty("current_weapon", currentWeapon.name);
            } else {
                json.addProperty("current_weapon", "");
            }
            json.add("skills", skills);
            return json;
        }catch (Exception e){
            System.err.println("err at toJson() for player");
        }
        return new JsonObject();
    }
}
