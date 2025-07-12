package rune.editor.objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;

public class Viewer {



    public Rectangle view;
    public Viewer(){
        view = new Rectangle();
    }

    public void setView(Camera camera){this.view.set(camera.position.x,camera.position.y,camera.viewportWidth,camera.viewportHeight);}














}
