package rune.editor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import rune.editor.entity.Entity;
import rune.editor.objects.Item;
import rune.editor.quest.Quest;
import rune.editor.scene.GameState;
import rune.editor.system.Inventory;
import rune.editor.types.DIRECTION;



import java.util.ArrayList;
import java.util.HashMap;


import static rune.editor.Game.keyBinds;
import static rune.editor.data.GameData.loadPlayerSaveFile;
import static rune.editor.types.DIRECTION.EAST;
import static rune.editor.types.DIRECTION.WEST;

public class Player {


    public final Texture textureSheet;
    private final Texture meleeSheet;
    private final Texture shadow;
    public final HashMap<String, Integer> attributeLevels = new HashMap<>();

    {
        attributeLevels.put("strength", 10);
        attributeLevels.put("intelligence", 10);
        attributeLevels.put("charisma", 10);
        attributeLevels.put("luck", 10);

    }


    private TextureRegion[] textureFrames;
    public Vector2 pos;
    public DIRECTION direction;
    public float speed = 100f;
    public float experience;
    private float delta;
    public float health = 100f;
    public float maxHealth = 100f;
    public boolean isAlive = true;
    public boolean isMoving, isMelee, isWeaponEquipped = false;

    public Rectangle bounds = new Rectangle(0, 0, 32, 32);
    public final Inventory inventory = new Inventory();
    public Circle range;

    public ArrayList<Quest> quests;
    public Quest activeQuest;
    public Entity lastEntityKilled;

    public String name;
    public String currentScene;
    private final ArrayList<Integer> hitEntities = new ArrayList<>();
    public Item currentWeapon;
    public Item torso;
    public Item legs;
    public Item helmet;
    public Item boots;
    public Item hands;

    public Player() {
        textureSheet = new Texture("player.png");
        meleeSheet = new Texture("heromelee.png");
        shadow = new Texture("shadow.png");

        textureFrames = TextureRegion.split(textureSheet, 32, 32)[0];
        pos = new Vector2(0, 0);

        direction = DIRECTION.SOUTH;


        quests = new ArrayList<>();
        range = new Circle(pos.x, pos.y, 32);


        loadPlayerSaveFile(this);
        System.out.println("Active quest name: " +activeQuest.name + "Journal entries" + activeQuest.journalEntries);
        System.out.println(isWeaponEquipped);
        System.out.println("Attribute levels: " + attributeLevels);


    }

    private void input(float dt) {
        isMoving = false;
        if (!isMelee) {
            if (Gdx.input.isKeyPressed(keyBinds.get("UP"))) {
                pos.y += speed * dt;
                direction = DIRECTION.NORTH;
                isMoving = true;
            } else if (Gdx.input.isKeyPressed(keyBinds.get("DOWN"))) {
                pos.y -= speed * dt;
                direction = DIRECTION.SOUTH;
                isMoving = true;
            } else if (Gdx.input.isKeyPressed(keyBinds.get("LEFT"))) {
                pos.x -= speed * dt;
                direction = DIRECTION.WEST;
                isMoving = true;
            } else if (Gdx.input.isKeyPressed(keyBinds.get("RIGHT"))) {
                pos.x += speed * dt;
                direction = EAST;
                isMoving = true;
            }

            if (Gdx.input.isKeyJustPressed(keyBinds.get("MELEE")) && isWeaponEquipped) {
                isMelee = true;
                delta = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            equipWeapon(inventory.getInventory().firstElement());
            System.out.println(inventory.getInventory().firstElement().name);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            unequipWeapon();
        }

    }


    public Rectangle wepRec = new Rectangle(0, 0, 32, 32);

    public boolean hitMob(Entity mob) {


        wepRec.set(0, 0, 0, 0);
        if (hitEntities.contains(mob.id())) {
            return false;
        }

        if (isWeaponEquipped && isMelee) {
            switch (direction) {
                case NORTH -> {
                    wepRec.set(getX(), getY() + 8, 32, 38);
                    if (wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0, 0, 0, 0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
                case WEST -> {
                    wepRec.set(getX() - 8, getY(), 38, 32);
                    if (wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0, 0, 0, 0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
                case EAST -> {
                    wepRec.set(getX() + 8, getY(), 38, 32);
                    if (wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0, 0, 0, 0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
                case SOUTH -> {
                    wepRec.set(getX(), getY() - 8, 32, 38);
                    if (wepRec.overlaps(mob.bounds)) {
                        mob.hurt = true;
                        wepRec.set(0, 0, 0, 0);
                        hitEntities.add(mob.id());
                        return true;
                    }
                }
            }
        }
        wepRec.set(0, 0, 0, 0);
        return false;
    }

    public void draw(Renderer renderer, float dt) {
        delta += Gdx.graphics.getDeltaTime();
        if (delta > 10) delta = 0;
        update(dt);
        if (isMelee) {
            final float shadowXDisplaced = pos.x + 16f;
            renderer.sb.draw(shadow, shadowXDisplaced, pos.y, shadow.getWidth() - 1, shadow.getHeight());
            renderer.sb.draw(getMeleeAnim().getKeyFrame(delta, isMelee), pos.x, pos.y);

            if (getMeleeAnim().isAnimationFinished(delta)) {
                isMelee = false;
                hitEntities.clear();
            }

        } else {
            final float shadowX = pos.x + 8.8f;
            final float shadowY = pos.y - 4;
            renderer.sb.draw(shadow, shadowX, shadowY, shadow.getWidth() - 1, shadow.getHeight());
            renderer.sb.draw(getAnim(delta).getKeyFrame(delta, isMoving), pos.x, pos.y);
        }

    }

    public void update(float dt) {
        input(dt);
        if (!isAlive) {
            die();
        }
        if (isMoving) {
            bounds.setPosition(pos.x, pos.y);
            range.setPosition(pos.x, pos.y);
        }
        if (activeQuest != null) {
            activeQuest.act(this);
        }
        if (!isWeaponEquipped) {
            isMelee = false;
        }
        quests.removeIf(quest -> quest.complete);
    }


    private Animation<TextureRegion> getMeleeAnim() {
        textureFrames = TextureRegion.split(meleeSheet, 48, 52)[0];
        return switch (direction) {
            case SOUTH -> new Animation<>(0.10f, textureFrames[2]);
            case NORTH -> new Animation<>(0.10f, textureFrames[3], textureFrames[4], textureFrames[3]);
            case WEST -> new Animation<>(0.10f, textureFrames[0], textureFrames[5]);
            case EAST -> new Animation<>(0.10f, textureFrames[1], textureFrames[6]);
        };
    }


    public Animation<TextureRegion> getAnim(float delta) {
        if (direction == EAST || direction == WEST) {
            textureFrames = TextureRegion.split(textureSheet, 32, 32)[1];
        } else if (direction == DIRECTION.NORTH || direction == DIRECTION.SOUTH) {
            textureFrames = TextureRegion.split(textureSheet, 32, 32)[0];
        }
        return switch (direction) {
            case SOUTH -> new Animation<>(0.25f, textureFrames[2], textureFrames[1], textureFrames[0]);
            case NORTH -> new Animation<>(0.25f, textureFrames[5], textureFrames[4], textureFrames[3]);
            case WEST -> new Animation<>(0.25f, textureFrames[5], textureFrames[4], textureFrames[5]);
            case EAST -> new Animation<>(0.25f, textureFrames[2], textureFrames[3], textureFrames[2]);
        };
    }

    public void completeQuest() {
        GameState.resetActiveQuest();
        activeQuest.complete(this);
        activeQuest = null;
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public void setActiveQuest(Quest quest) {
        activeQuest = quest;
        GameState.setActiveQuest(activeQuest);
    }

    private void die() {
        isMelee = false;
        isMoving = false;
        hitEntities.clear();
        if (experience > 0) {
            experience = Math.max(0, experience - 50);
        }
    }

    public static Player New() {
        return new Player();
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public boolean takeDamage(float damage) {
        health -= damage;

        if (health <= 0) {
            health = 0;
            isAlive = false;
        }

        return isAlive;
    }

    public void addItem(Item i) {
        inventory.addItems(i);
    }

    public void heal(float amount) {
        health += amount;

    }

    public void equipWeapon(String weaponName) {
        if (this.inventory.getItem(weaponName) != null) {
            this.currentWeapon = inventory.getItem(weaponName);
            isWeaponEquipped = true;
        }
    }

    public void unequipWeapon() {
        isWeaponEquipped = false;
        this.currentWeapon = null;
    }

    public void equipWeapon(Item i) {
        currentWeapon = i;
        isWeaponEquipped = true;
        if (!inventory.getInventory().contains(i)) {
            addItem(i);
        }
    }

    public void consumePotion(Item potion) {
        if (potion.effect.getKey().equals("health")) {
            heal(potion.effect.getValue());
        }
        if (potion.effect.getKey().equals("strength")) {
            attributeLevels.put("strength", attributeLevels.get("strength") + potion.effect.getValue());
        }
        if (potion.effect.getKey().equals("intelligence")) {
            attributeLevels.put("intelligence", attributeLevels.get("intelligence") + potion.effect.getValue());
        }
    }

    public JsonObject journalJson() {
        try {
            final JsonObject json = new JsonObject();
            for (Quest quest : quests) {
                json.addProperty("quest", quest.name);
                json.addProperty("complete", quest.complete);
                JsonArray jJournalEntries = new JsonArray();
                quest.journalEntries.forEach((key, value) -> {
                    JsonObject jJournalEntry = new JsonObject();
                    jJournalEntry.addProperty("date", key);
                    jJournalEntry.addProperty("entry", value);
                    jJournalEntries.add(jJournalEntry);
                });
                json.add("entries", jJournalEntries);
                return json;
            }
        } catch (Exception e) {
            System.err.println("err at `Player::journalJson()`");
        }
        return new JsonObject();
    }

    public JsonObject inventoryJson() {
        try {
            final JsonObject json = new JsonObject();
            final JsonArray jInventory = new JsonArray(inventory.getInventory().size());


            json.add("equipment slots", equipmentJson());

            inventory.sortedInventory.forEach((item, amount) -> {
                JsonObject jInventoryEntry = new JsonObject();
                jInventoryEntry.addProperty("name", item);
                jInventoryEntry.addProperty("amount", amount);
                jInventory.add(jInventoryEntry);
            });
            json.add("items", jInventory);
            return json;
        } catch (Exception e) {
            System.err.println("err at `Player::inventoryJson()`");
        }
        return new JsonObject();
    }

    public JsonObject equipmentJson() {
        try {
            final JsonObject equipmentSlots = new JsonObject();
            if (currentWeapon != null) {
                equipmentSlots.addProperty("melee", currentWeapon.name);
            } else {
                equipmentSlots.addProperty("melee", "");
            }
            if (helmet != null) {
                equipmentSlots.addProperty("helmet", helmet.name);
            } else {
                equipmentSlots.addProperty("helmet", "");
            }
            if (torso != null) {
                equipmentSlots.addProperty("torso", torso.name);
            } else {
                equipmentSlots.addProperty("torso", "");
            }
            if (legs != null) {
                equipmentSlots.addProperty("legs", legs.name);
            } else {
                equipmentSlots.addProperty("legs", "");
            }
            if (boots != null) {
                equipmentSlots.addProperty("boots", boots.name);
            } else {
                equipmentSlots.addProperty("boots", "");
            }
            return equipmentSlots;
        } catch (Exception e) {
            System.err.println("err at `Player::equipmentJson()`");
        }
        return new JsonObject();
    }

    public JsonObject toJson() {
        try {
            final JsonObject json = new JsonObject();
            final JsonObject jSkills = new JsonObject();
            jSkills.addProperty("strength", attributeLevels.get("strength"));
            jSkills.addProperty("intelligence", attributeLevels.get("intelligence"));
            jSkills.addProperty("charisma", attributeLevels.get("charisma"));
            jSkills.addProperty("luck", attributeLevels.get("luck"));
            json.addProperty("name", name);
            json.addProperty("posX", pos.x);
            json.addProperty("posY", pos.y);
            json.addProperty("maxHealth", maxHealth);
            json.addProperty("currentHealth", health);
            json.addProperty("experience", experience);
            if (activeQuest != null) {
                json.addProperty("activeQuest", activeQuest.name);
            } else {
                json.addProperty("activeQuest", "");
            }

            json.add("skills", jSkills);

            return json;

        } catch (Exception e) {
            System.err.println("err at `Player::toJson()`");
        }
        return new JsonObject();
    }

    public JsonObject statusJson(){
        final JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("posX", pos.x);
        json.addProperty("posY", pos.y);
        json.addProperty("health", health);
        return json;
    }
    public JsonObject syncJson(){
        final JsonObject json = new JsonObject();
        json.addProperty("activeScene",currentScene);
        json.add("player", statusJson());
        return json;
    }
}
