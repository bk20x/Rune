package rune.editor.types;

public enum Rarity {

    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
    UNIQUE;

    public static Rarity fromString(String s){
        return Rarity.valueOf(s.toUpperCase());
    }

}
