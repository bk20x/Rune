package rune.editor.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import rune.editor.Renderer;

public class Tile {




    public Texture texture;
    public Rectangle bounds;
    float x, y;
    public Tile(String name,float x, float y){
        this.x = x;
        this.y = y;
        this.texture = new Texture("tiles/" + name + ".png");
        this.bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }

    public void draw(OrthographicCamera camera, Renderer renderer){
        renderer.sb.setProjectionMatrix(camera.combined);
        renderer.sb.draw(texture, x, y);
    }

    public static Tile New(String name, float x, float y){
        return new Tile(name,x,y);
    }



}
