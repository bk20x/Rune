package rune.editor;

public class ShapeRenderer extends com.badlogic.gdx.graphics.glutils.ShapeRenderer {

    public ShapeRenderer(){
        super();
    }
    public void start(){
        super.begin(ShapeType.Line);
    }
    public void stop(){
        super.end();
    }
    public static ShapeRenderer New(){return new ShapeRenderer();}


}
