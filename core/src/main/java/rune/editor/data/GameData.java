package rune.editor.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
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

        JsonObject playerData = objFromFile("json/player/player.json", JsonObject.class);
        int charisma = playerData.get("skills").getAsJsonObject().get("charisma").getAsInt();
        int strength = playerData.get("skills").getAsJsonObject().get("strength").getAsInt();
        int intelligence = playerData.get("skills").getAsJsonObject().get("intelligence").getAsInt();
        int luck = playerData.get("skills").getAsJsonObject().get("luck").getAsInt();
        player.attributeLevels.put("charisma", charisma);
        player.attributeLevels.put("strength", strength);
        player.attributeLevels.put("intelligence", intelligence);
        player.attributeLevels.put("luck", luck);

    }
    public static void loadPlayerSaveFile(Player player){
        try {


            JsonObject playerData = objFromFile("json/player/player.json", JsonObject.class);

            String name = playerData.get("name").getAsString();
            float experience = playerData.get("experience").getAsFloat();

            String currentWeapon = playerData.get("current_weapon").getAsString();

            player.name = name;
            player.experience = experience;
            player.pos.x = playerData.get("posX").getAsFloat();
            player.pos.y = playerData.get("posY").getAsFloat();

            if (currentWeapon != null && !currentWeapon.isEmpty()) {

                player.currentWeapon = Items.Weapon(playerData.get("current_weapon").getAsString()); //Items.Weapon() for testing, this will check the future inventory file for the item and set the current Weapon based on that
                player.isWeaponEquipped = true;

            }
            loadPlayerStats(player);
            loadPlayerQuests(player);
        } catch ( RuntimeException e ){
            System.err.println("Error parsing save file: " + e);
        }


    }

    public static void loadPlayerQuests(Player player){
        for (JsonElement element : arrayFromFile("json/player/journal.json")){
            JsonObject questData = element.getAsJsonObject();
            final Quest quest = new Quest(questData.get("quest").getAsString());
            System.out.println(questData.get("quest").getAsString());
            quest.complete = questData.get("complete").getAsBoolean();
            for (JsonElement entry : questData.getAsJsonArray("entries")) {
                quest.journalEntries.put(entry.getAsJsonObject().get("date").getAsString(), entry.getAsJsonObject().get("entry").getAsString());

                player.quests.add(quest);
            }
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
