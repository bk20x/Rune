package rune.editor.quest;

import rune.editor.Player;
import rune.editor.data.GameData;
import rune.editor.lua.Interop;
import rune.editor.objects.Item;
import rune.editor.objects.Items;
import rune.editor.types.Rarity;

import java.util.ArrayList;


public class Quest {


    public String name;
    public Rarity rarity;
    public String[] rewards;
    public String objective;

    public ArrayList<String> journalEntries;
    public String script;

    public boolean complete = false;




   public Quest(String name){
        this.name = name;
        this.journalEntries = new ArrayList<>();
        GameData.setQuestValues(this);
   }

   public void complete(Player player){
       giveRewards(player);
       complete = true;
   }

   public void act(Player player){

   }
   public void giveRewards(Player player){
        for (String s : rewards){
            if(s.contains("random")){
                var amount = Integer.parseInt(s.split(":")[1]);
                for (int i = 0; i < amount; i++) {
                    player.inventory.addItems(Items.random(rarity));
                }
            }
            else if(s.contains("exp")){
                player.experience += Integer.parseInt(s.split(":")[1]);
            }
            else {
                Item item = new Item(s.split(":")[0]);
                var amount = Integer.parseInt(s.split(":")[1]);
                for (int i = 0; i < amount; i++) {
                    player.inventory.addItems(item);
                }
            }
        }
   }



   public void loadScript(Interop interop){
       interop.loadLuaFile(script);
   }










}
