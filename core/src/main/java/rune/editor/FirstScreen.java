package rune.editor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ScreenUtils;
import rune.editor.shaders.Shaders;

import static rune.editor.State.*;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {


    private State game;

    public FirstScreen(State game) {
        this.game = game;
    }

    ShaderProgram test = Shaders.Test();
    @Override
    public void show() {


        loadLuaFile("lua/config.lua");
        loadLuaFile("lua/gamescript.lua");


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.7f,0.7f,0.7f,1);


        callLuaFunc("draw");
        if(activeQuest != null){
            loadLuaFile("lua/quests/" + activeQuest + ".lua");
        }

    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.

    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

    public void loadLuaFile(String filePath) {
        game.interop.loadLuaFile(filePath);
    }
    public void callLuaFunc(String funcName) {

        game.interop.callLuaFunc(funcName);

    }

}
