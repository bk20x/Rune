package rune.editor.types;

import com.badlogic.gdx.math.MathUtils;

public enum DIRECTION {

    NORTH,
    SOUTH,
    EAST,
    WEST;

    public static DIRECTION fromString(String s){
        return DIRECTION.valueOf(s.toUpperCase());
    }
    public static DIRECTION fromInt(int i){
        return DIRECTION.values()[i];
    }
    public static DIRECTION Random(){
        return DIRECTION.values()[MathUtils.random(0,3)];
    }
}
