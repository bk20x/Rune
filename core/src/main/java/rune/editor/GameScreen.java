package rune.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import rune.editor.external_lib.NimLib;
import rune.editor.scene.GameState;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {


    private final Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    public static final GameState gameState = new GameState();
    public Renderer renderer;

    @Override
    public void show() {

        renderer =  new Renderer();
        loadLuaFile("lua/config.lua");
        loadLuaFile("lua/main.lua");
    }

    @Override
    public void render(float delta) {



        ScreenUtils.clear(0.7f,0.7f,0.7f,1);
        gameState.run(renderer,delta);
        callLuaFunc("main");
        reloadScript();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (game != null && game.interop != null) {
            game.interop.dispose();
        }
        gameState.scene.dispose();
    }

    public void loadLuaFile(String filePath) {
        game.interop.loadLuaFile(filePath);
    }
    public void callLuaFunc(String funcName) {
        game.interop.callLuaFunc(funcName);
    }

    public void reloadScript(){
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            loadLuaFile("lua/main.lua");
        }

    }


}
