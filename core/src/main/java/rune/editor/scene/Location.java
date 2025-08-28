package rune.editor.scene;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Location {

    public Rectangle bounds;
    public Vector2 pos;
    protected String targetScene;

    public Location(String targetScene, Vector2 pos, Rectangle bounds) {
        this.targetScene = targetScene;
        this.pos = pos;
        this.bounds = bounds;
    }


    public Scene getTargetScene() {
        return new Scene(targetScene);
    }




}
