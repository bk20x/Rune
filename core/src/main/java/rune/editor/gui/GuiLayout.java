package rune.editor.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiLayout {



    public HashMap<Integer,GuiElement> table;
    public int triggeredElementId;

    public GuiLayout() {
        table = new HashMap<>();

    }


    public void registerClicks(Vector2 mousePos){
        table.forEach((k,v)->{
            int elementId = v.trigger(mousePos);
            if(elementId != 0){
                triggeredElementId = elementId;
            }
            else {
                triggeredElementId = 0;
            }
        });
    }
    public void addElement(GuiElement element){
        table.put(element.id,element);
    }

    public void removeElement(int id){
        table.forEach((k,v)->{
            if(k == id){
                removeElement(v);
            }
        });
    }

    private void removeElement(GuiElement element){
        table.remove(element.id);
    }
    public void update() {

    }

    public void draw() {

    }

    public static GuiLayout New() {
        return new GuiLayout();
    }


}
