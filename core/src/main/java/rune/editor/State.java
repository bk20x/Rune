package rune.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import rune.editor.lua.Interop;

import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class State extends Game {

    public static HashMap<String, Integer> keyBinds = new HashMap<>();


    public static void setKeybind(String key, int value){keyBinds.put(key,value);}
    public static int getWindowWidth(){
        return Gdx.graphics.getWidth();
    }
    public static int getWindowHeight(){
        return Gdx.graphics.getHeight();
    }

    public Interop interop;
    public static String activeQuest;

    @Override
    public void create() {
        interop = new Interop();
        setScreen(new FirstScreen(this));
    }


}
