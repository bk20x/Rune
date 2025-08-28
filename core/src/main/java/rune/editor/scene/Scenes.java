package rune.editor.scene;

import rune.editor.maps.DungeonMap;

public class Scenes {





    public static Scene Intro(){return new Scene("Intro");}
    public static DungeonMap Dungeon(String name){
        return new DungeonMap(name);
    }
    public static Scene New(String name){return new Scene(name);}

}
