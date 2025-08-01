package rune.editor.types;

public enum NpcTypes {

    MERCHANT,
    VILLAGER,
    GUARD,
    STORY;


    public static NpcTypes fromString(String s){
        return NpcTypes.valueOf(s.toUpperCase());
    }


}
