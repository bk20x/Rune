package rune.editor.data;

import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kotlin._Assertions;
import net.dermetfan.gdx.physics.box2d.PositionController;
import net.dermetfan.utils.Pair;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.ast.Str;
import rune.editor.Player;
import rune.editor.npc.Npc;
import rune.editor.entity.Entity;
import rune.editor.objects.Item;
import rune.editor.objects.Items;
import rune.editor.quest.Quest;
import rune.editor.types.ItemTypes;
import rune.editor.types.NpcTypes;
import rune.editor.types.Rarity;

import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;

public class GameData {


    public static Gson gson = new Gson();


    public static  <T> T objFromFile(String filePath, Class<T> classOfT) {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, classOfT);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T objFromString(String json, Class<T> classOfT) {return gson.fromJson(json, classOfT);}
    public static JsonArray arrayFromFile(String filePath) {
        return objFromFile(filePath, JsonArray.class);
    }

    public static void setMobValues(Entity e){
        for (JsonElement element :  arrayFromFile("json/mobs.json")) {
            JsonObject entityData = element.getAsJsonObject();
            if(entityData.get("name").getAsString().equals(e.name)) {
                e.setHealth(entityData.get("health").getAsFloat());
                e.setDamage(entityData.get("damage").getAsFloat());
                e.setRarity(Rarity.valueOf(entityData.get("rarity").getAsString().toUpperCase()));
                e.setSpeed(entityData.get("speed").getAsFloat());
            }
        }
    }



    public static void setNpcValues(Npc n){
        for(JsonElement element : arrayFromFile("json/npcs.json")){
            JsonObject npcData = element.getAsJsonObject();
            if(npcData.get("name").getAsString().equals(n.name)) {
                n.setType(NpcTypes.fromString(npcData.get("type").getAsString()));
            }
        }
    }


    public static void setQuestValues(Quest q){
        for (JsonElement element : arrayFromFile("json/quests.json")) {
            JsonObject questData = element.getAsJsonObject();
            if(questData.get("name").getAsString().equals(q.name)) {
                q.rewards = questData.get("rewards").getAsString().split(";");
                q.objective = questData.get("objective").getAsString();
            }
        }

    }
    public static void loadPlayerStats(Player player){

            JsonObject obj = objFromFile("json/player/player.json", JsonObject.class);



    }
    public static void loadPlayerSaveFile(Player player){
        try {


            JsonObject playerData = objFromFile("json/player/player.json", JsonObject.class);
            String name = playerData.get("name").getAsString();
            float experience = playerData.get("experience").getAsFloat();
            int charisma = playerData.get("skills").getAsJsonObject().get("charisma").getAsInt();
            int strength = playerData.get("skills").getAsJsonObject().get("strength").getAsInt();
            int intelligence = playerData.get("skills").getAsJsonObject().get("intelligence").getAsInt();
            int luck = playerData.get("skills").getAsJsonObject().get("luck").getAsInt();
            String currentWeapon = playerData.get("current_weapon").getAsString();

            player.name = name;
            player.experience = experience;
            player.pos.x = playerData.get("pos_x").getAsFloat();
            player.pos.y = playerData.get("pos_y").getAsFloat();
            if (currentWeapon != null && !currentWeapon.isEmpty()) {

                player.currentWeapon = Items.weapon(playerData.get("current_weapon").getAsString()); //Items.weapon() for testing, this will check the future inventory file for the item and set the current weapon based on that
                player.isWeaponEquipped = true;
            }
            player.attributeLevels.put("charisma", charisma);
            player.attributeLevels.put("strength", strength);
            player.attributeLevels.put("intelligence", intelligence);
            player.attributeLevels.put("luck", luck);
        } catch ( RuntimeException e2) {
            System.err.println("Player save file is corrupt");
        }


    }

    public static void loadPlayerQuests(Player player){
        Quest quest = null;
        for (JsonElement element : arrayFromFile("json/player/active_quests.json")) {
            JsonObject questData = element.getAsJsonObject();
            quest = new Quest(questData.getAsJsonObject().get("name").getAsString());
            quest.complete = questData.get("complete").getAsBoolean();
            for (JsonElement entry : questData.get("journal_entries").getAsJsonArray().asList()) {
                quest.journalEntries.add(entry.getAsString());
            }

        }
        if(quest != null) {
            player.quests.add(quest);
        }
    }

    @Deprecated
    @Nullable
    public static ItemTypes getItemType(String name){
        for (JsonElement element : arrayFromFile("json/items.json")) {
            JsonObject itemData = element.getAsJsonObject();
            if (itemData.get("name").getAsString().equals(name)) {
                return ItemTypes.fromString(itemData.get("type").getAsString());
            }
        }
        return null;
    }


    public static void setItemValues(Item i){
        for(JsonElement element : arrayFromFile("json/items.json")){
            JsonObject itemData = element.getAsJsonObject();
            if (itemData.get("name").getAsString().equals(i.name)) {
                i.setType(ItemTypes.fromString(itemData.get("type").getAsString()));
                i.setRarity(Rarity.fromString(itemData.get("rarity").getAsString()));
                switch (i.type){
                    case WEAPON -> i.setBaseDamage(itemData.get("damage").getAsFloat());
                    case POTION -> {
                        String effect = itemData.get("effect").getAsString().split(":")[0];
                        float value = Float.parseFloat(itemData.get("effect").getAsString().split(":")[1]);
                        i.setEffect(effect,value);
                    }
                }
            }
        }
    }



}
