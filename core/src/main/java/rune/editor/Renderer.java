package rune.editor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer {

    public SpriteBatch sb;

    public Renderer(){
        sb = new SpriteBatch();
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

}
