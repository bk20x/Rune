package rune.editor.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import rune.editor.Player;
import rune.editor.external_lib.NimLib;
import rune.editor.magic.Spell;
import rune.editor.npc.Npc;
import rune.editor.entity.Entity;
import rune.editor.objects.Item;
import rune.editor.objects.Items;
import rune.editor.quest.Quest;
import rune.editor.scene.Scene;
import rune.editor.types.ItemTypes;
import rune.editor.types.NpcTypes;
import rune.editor.types.Rarity;
import rune.editor.types.SpellTypes;

import java.io.*;
import java.util.Arrays;

public class GameData {


    public static final Gson gson = new Gson();
    public static final String savePath = "json/player/save.json";

    public static final NimLib preload = NimLib.Instance;
    static {
        System.load(NimLib.PATH);
    }
    public static <T> T objFromFile(String filePath, Class<T> classOfT) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, classOfT);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T objFromString(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static JsonArray arrayFromFile(String filePath) {
        return objFromFile(filePath, JsonArray.class);
    }

    public static void setMobValues(Entity e) {
        JsonObject entityData = objFromString(preload.queryMob(e.name), JsonObject.class);
        e.setHealth(entityData.get("health").getAsInt());
        e.setDamage(entityData.get("damage").getAsFloat());
        e.setRarity(Rarity.valueOf(entityData.get("rarity").getAsString().toUpperCase()));
        e.setSpeed(entityData.get("speed").getAsFloat());


    }

    public static void setItemValues(Item i) {
        JsonObject itemData = objFromString(preload.queryItem(i.name), JsonObject.class);
        i.setType(ItemTypes.fromString(itemData.get("type").getAsString()));
        i.setRarity(Rarity.fromString(itemData.get("rarity").getAsString()));
        i.cost = itemData.get("cost").getAsInt();
        switch (i.type) {
            case WEAPON -> i.setBaseDamage(itemData.get("damage").getAsFloat());
            case POTION -> {
                final String effect = itemData.get("effect").getAsString().split(":")[0];
                final int value = Integer.parseInt(itemData.get("effect").getAsString().split(":")[1]);
                if (itemData.get("duration").getAsInt() > 0) {
                    i.duration = itemData.get("duration").getAsInt();
                }
                i.setEffect(effect, value);
            }
            case ARMOR -> i.defense = itemData.get("defense").getAsInt();
        }

    }

    public static void setSpellValues(Spell s) {
        for (JsonElement element : arrayFromFile("json/spells.json")) {
            JsonObject spellData = element.getAsJsonObject();
            if (spellData.get("name").getAsString().equals(s.name)) {
                s.type = SpellTypes.fromString(spellData.get("type").getAsString());
                s.damage = spellData.get("damage").getAsFloat();
                s.range = spellData.get("range").getAsFloat();
                s.travelSpeed = spellData.get("travel speed").getAsFloat();
                if (s.range != 0) {
                    s.isRanged = true;
                }
            }
        }
    }

    public static void setObjectValues(Entity e) {
        new Thread(() -> {
            for (JsonElement element : arrayFromFile("json/objects.json")) {
                JsonObject entityData = element.getAsJsonObject();
                if (entityData.get("name").getAsString().equals(e.name)) {
                }
            }
        }).start();
    }
    public static void setNpcValues(Npc n) {
        for (JsonElement element : arrayFromFile("json/npcs.json")) {
            JsonObject npcData = element.getAsJsonObject();
            if (npcData.get("name").getAsString().equals(n.name)) {
                n.setType(NpcTypes.fromString(npcData.get("type").getAsString()));
            }
        }
    }


    public static void setQuestValues(Quest q) {
        for (JsonElement element : arrayFromFile("json/quests.json")) {
            JsonObject questData = element.getAsJsonObject();
            if (questData.get("name").getAsString().equals(q.name)) {
                q.rewards = questData.get("rewards").getAsString().split(";");
                q.objective = questData.get("objective").getAsString();
            }
        }

    }

    public static void loadPlayerStats(Player player) {

        final JsonObject playerData = objFromFile("json/player/player.json", JsonObject.class);
        final int charisma = playerData.get("skills").getAsJsonObject().get("charisma").getAsInt();
        final int strength = playerData.get("skills").getAsJsonObject().get("strength").getAsInt();
        final int intelligence = playerData.get("skills").getAsJsonObject().get("intelligence").getAsInt();
        final int luck = playerData.get("skills").getAsJsonObject().get("luck").getAsInt();
        player.attributeLevels.put("charisma", charisma);
        player.attributeLevels.put("strength", strength);
        player.attributeLevels.put("intelligence", intelligence);
        player.attributeLevels.put("luck", luck);

    }

    public static void loadPlayerSaveFile(Player player) {
        try {


            final JsonObject playerData = objFromFile("json/player/save.json", JsonObject.class);

            final String name = playerData.get("name").getAsString();
            final float experience = playerData.get("experience").getAsFloat();
            player.name = name;
            player.experience = experience;
            player.pos.x = playerData.get("posX").getAsFloat();
            player.pos.y = playerData.get("posY").getAsFloat();
            player.maxHealth = playerData.get("maxHealth").getAsFloat();
            player.health = playerData.get("currentHealth").getAsFloat();

            JsonObject inventoryData = objFromFile("json/player/inventory.json", JsonObject.class);
            JsonObject equipmentSlots = inventoryData.get("equipment slots").getAsJsonObject();


            if (!equipmentSlots.get("melee").getAsString().isBlank()) {
                player.equipWeapon(Items.Weapon(equipmentSlots.get("melee").getAsString()));
            }
            if (!equipmentSlots.get("helmet").getAsString().isBlank()) {
                var helmet = Items.Armor(equipmentSlots.get("helmet").getAsString());
                player.helmet = helmet;
                player.addItem(helmet);
            }
            if (!equipmentSlots.get("torso").getAsString().isBlank()) {
                var torso = Items.Armor(equipmentSlots.get("torso").getAsString());
                player.torso = torso;
                player.addItem(torso);
            }
            if (!equipmentSlots.get("legs").getAsString().isBlank()) {
                var legs = Items.Armor(equipmentSlots.get("legs").getAsString());
                player.legs = legs;
                player.addItem(legs);
            }
            if (!equipmentSlots.get("boots").getAsString().isBlank()) {
                var boots = Items.Armor(equipmentSlots.get("boots").getAsString());
                player.boots = boots;
                player.addItem(boots);
            }

            loadPlayerStats(player);
            loadPlayerQuests(player);

        } catch (RuntimeException e) {
            System.err.println("Error parsing save file: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void loadPlayerQuests(Player player) {
        for (JsonElement element : arrayFromFile("json/player/journal.json")) {
            JsonObject questData = element.getAsJsonObject();
            final Quest quest = new Quest(questData.get("quest").getAsString());
            System.out.println(questData.get("quest").getAsString());
            quest.complete = questData.get("complete").getAsBoolean();
            if(questData.get("active").getAsBoolean()) {
                player.activeQuest = quest;
            }
            for (JsonElement entry : questData.getAsJsonArray("entries")) {
                quest.journalEntries.put(entry.getAsJsonObject().get("date").getAsString(), entry.getAsJsonObject().get("entry").getAsString());
            }
            player.quests.add(quest);
        }


    }


    public static void setSceneValues(Scene s) {
        for (JsonElement element : arrayFromFile("json/scenes.json")) {
            JsonObject sceneData = element.getAsJsonObject();
            if (sceneData.get("scene").getAsString().equals(s.name)) {

            }
        }
    }

    public static void writeInventoryFile(Player player) {
        JsonObject inventoryJson = player.inventoryJson();
        try (FileOutputStream fos = new FileOutputStream("json/player/inventory.json")) {
            fos.write(inventoryJson.toString().getBytes());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void writePlayerSaveFile(Player player) {
        JsonObject playerJson = player.toJson();
        try (FileOutputStream fos = new FileOutputStream(savePath)) {
            fos.write(playerJson.toString().getBytes());
        } catch (IOException e) {
            System.err.println(e);
        }
        writeInventoryFile(player);
    }

}
