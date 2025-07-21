package rune.editor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import rune.editor.scene.GameState;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {


    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    public static GameState runeGame = new GameState();
    @Override
    public void show() {


        loadLuaFile("lua/config.lua");
        loadLuaFile("lua/gamescript.lua");


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.7f,0.7f,0.7f,1);

        callLuaFunc("draw");


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


        if (runeGame != null) {
            if (runeGame.scene != null) {
                runeGame.scene.dispose();
            }
        }
    }

    public void loadLuaFile(String filePath) {
        game.interop.loadLuaFile(filePath);
    }
    public void callLuaFunc(String funcName) {
        game.interop.callLuaFunc(funcName);
    }




}
