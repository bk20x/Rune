package rune.editor.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rune.editor.data.GameData;
import rune.editor.types.NpcTypes;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import static rune.editor.data.GameData.setNpcValues;

public class Npc {

    public String name;
    public NpcTypes type;
    public String currentBranch;

    public Vector2 pos;
    public Texture texture;
    public Animation<TextureRegion> animation;

    public Npc(String name){
        this.name = name;
        this.pos = new Vector2();
        setBranchToDefault();
        setNpcValues(this);

    }


    public Npc(){


    }

    public List<String> getBranchOptions(String branch){
        List<String> options = new ArrayList<>();
        for(JsonElement elem: GameData.arrayFromFile("json/dialogues.json")){
            JsonObject dialogue = elem.getAsJsonObject();
            if(dialogue.get("actor").getAsString().equals(name)) {
                for(JsonElement branchElem: dialogue.get("branches").getAsJsonArray()) {
                    JsonObject dialogueBranch = branchElem.getAsJsonObject();
                    if(dialogueBranch.getAsJsonObject().get("branch").getAsString().equals(branch)){
                        for(JsonElement optionElem: dialogueBranch.getAsJsonObject().get("options").getAsJsonArray()){
                            JsonObject optionData = optionElem.getAsJsonObject();
                            options.add(optionData.get("option").getAsString());
                        }
                    }
                }
            }
        }
        return options;
    }


    public List<String> getCurrentBranchOptions(){
        return getBranchOptions(currentBranch);
    }

    public String getDialogueResponseAndWalkTree(String option){
        for(JsonElement elem: GameData.arrayFromFile("json/dialogues.json")){
            JsonObject dialogue = elem.getAsJsonObject();
            if(dialogue.get("actor").getAsString().equals(name)) {
                for(JsonElement branchElem: dialogue.get("branches").getAsJsonArray()) {
                    JsonObject dialogueBranch = branchElem.getAsJsonObject();
                    if(dialogueBranch.getAsJsonObject().get("branch").getAsString().equals(currentBranch)){
                        for(JsonElement optionElem: dialogueBranch.getAsJsonObject().get("options").getAsJsonArray()){
                            JsonObject optionData = optionElem.getAsJsonObject();
                            if(!optionData.get("next").getAsString().equals("return")){
                                setCurrentBranch(optionData.get("next").getAsString());
                            }
                            else {
                                setBranchToDefault();
                            }
                            if(optionData.get("option").getAsString().equals(option)){
                                return optionData.get("response").getAsString();
                            }
                        }
                   }
                }
            }
        }
        return "Invalid Dialogue Option";
    }

    public void setCurrentBranch(String branch){this.currentBranch = branch;}
    public void setBranchToDefault(){this.currentBranch = "default";}
    public void setType(NpcTypes type){this.type = type;}

    public static Npc New(String name){
        return new Npc(name);
    }

    public static Npc Empty(){
        return new Npc();
    }
}
