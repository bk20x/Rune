package rune.editor.types;

public enum NpcTypes {

    Merchant,
    Villager,
    Guard,
    Story;


    public static NpcTypes fromString(String s){
        return NpcTypes.valueOf(s.toUpperCase());
    }


}
