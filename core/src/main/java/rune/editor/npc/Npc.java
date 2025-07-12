package rune.editor.npc;

import rune.editor.types.NpcTypes;

import java.util.HashMap;

import static rune.editor.data.GameData.setNpcValues;

public class Npc {

    public String name;
    public NpcTypes type;


    public Npc(String name){
        this.name = name;


        setNpcValues(this);

    }




    public void setType(NpcTypes type){this.type = type;}


}
