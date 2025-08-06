package rune.editor.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import rune.editor.Renderer;


public class Button extends GuiElement{


    public GuiListener listener;
    private TextureRegion upRegion, downRegion;
    public BitmapFont font;
    public Rectangle bounds;
    public int id;
    public boolean isDown = false;
    public Vector2 pos;
    public Button(int id, String upStyle, String downStyle) {


        this.id = id;
        this.pos = new Vector2();
        font = new BitmapFont();
        upRegion = new TextureRegion(new Texture("ui/" + upStyle + ".png"));
        downRegion = new TextureRegion(new Texture("ui/" + downStyle + ".png"));
        bounds = new Rectangle(0, 0, upRegion.getRegionWidth(), upRegion.getRegionHeight());

        addListener(() -> System.err.println("Button pressed: " + id));



    }
    public void setPos(float x, float y){
        pos.set(x,y);
    }
    public void setBounds(float x, float y, float width, float height) {
        bounds.set(x, y, 100, 100);
    }
    public void setBoundsToCam(OrthographicCamera camera){
        setBounds(camera.unproject(new Vector3(pos.x, pos.y, 0)).x, camera.unproject(new Vector3(pos.x, pos.y, 0)).y, upRegion.getRegionWidth(), upRegion.getRegionHeight());
    }
    @Override
    public int trigger(Vector2 mousePos){
        if(mousePos.x > bounds.x && mousePos.x < bounds.x + bounds.width && mousePos.y > bounds.y && mousePos.y < bounds.y + bounds.height){
            if(Gdx.input.justTouched()) {
                isDown = true;
                this.listener.guiEvent();
                return this.id;
            }
            else if(isDown){
                isDown = false;
            }
        }
        return 0;
    }


    public void draw(Renderer renderer, float x, float y) {
        renderer.sb.draw(upRegion, x, y, upRegion.getRegionWidth(), upRegion.getRegionHeight());
    }

    public void addListener(GuiListener listener) {
        this.listener = listener;
    }

    public static Button New(int id, String upStyle, String downStyle) {
        return new Button(id, upStyle, downStyle);
    }

}
