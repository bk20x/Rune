package rune.editor.types;

public enum SpellTypes {


    DESTRUCTION;




    public static SpellTypes fromString(String string) {
        return SpellTypes.valueOf(string.toUpperCase());
    }

}
