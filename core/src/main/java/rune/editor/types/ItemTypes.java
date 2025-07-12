package rune.editor.types;

public enum ItemTypes {


    WEAPON,
    ARMOR,
    MISC,
    POTION;


    public static ItemTypes fromString(String s){
        return ItemTypes.valueOf(s.toUpperCase());
    }

}
