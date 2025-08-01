package rune.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer {

    public SpriteBatch sb;
    public Texture whitePixel;

    public Renderer(){
        sb = new SpriteBatch();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);

        pixmap.dispose();
    }

    public static Renderer New(){return new Renderer();}

    public void setView(OrthographicCamera camera){
        sb.setProjectionMatrix(camera.combined);
    }

    public void start(){
        sb.begin();
    }

    public void stop(){
        sb.end();
    }

    public void flush(){
        sb.flush();
    }


    public void fillScreen(Color color) {
        Color oldColor = sb.getColor();
        sb.setColor(color);
        sb.draw(whitePixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.setColor(oldColor);
    }


    public void dispose() {
        if (whitePixel != null) {
            whitePixel.dispose();
        }
        if (sb != null) {
            sb.dispose();
        }
    }
}
