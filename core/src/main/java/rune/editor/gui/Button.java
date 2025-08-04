package rune.editor.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import rune.editor.Renderer;


public class Button {


    public ButtonListener listener;
    private TextureRegion upRegion, downRegion;
    public BitmapFont font;
    public String text;
    public Rectangle bounds;

    public Button(String text, String upStyle, String downStyle) {


        this.text = text;
        font = new BitmapFont();
        upRegion = new TextureRegion(new Texture("gui/button/" + upStyle + ".png"));
        downRegion = new TextureRegion(new Texture("gui/button/" + downStyle + ".png"));
        addListener(buttonId -> System.err.println("Button pressed: " + buttonId));

    }


    public void draw(Renderer renderer, float x, float y) {
        font.draw(renderer.sb,text, x, y);
    }

    public void addListener(ButtonListener listener) {
        this.listener = listener;
    }

}
