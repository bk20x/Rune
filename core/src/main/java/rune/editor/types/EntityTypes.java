package rune.editor.types;

public enum EntityTypes {
    STATIC_OBJ,
    ANIMATED_OBJ,
    MOB;

    public static EntityTypes fromString(String s){
        return EntityTypes.valueOf(s.toUpperCase());
    }

}
