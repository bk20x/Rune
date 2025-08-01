package rune.editor.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GuiListener {




    public boolean isListening = false;
    private boolean wasMouseDown = false;


    public GuiListener() {

    }






    public Vector2 getMousePos(OrthographicCamera cam) {
        // Get screen coordinates (origin at top-left)
        Vector3 screenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        // Debug raw screen coordinates
        System.out.println("[DEBUG_MOUSE] Screen coordinates: " + screenCoords.x + ", " + screenCoords.y);

        // Convert to world coordinates (origin at bottom-left, affected by camera position/zoom)
        Vector3 mousePos = cam.unproject(screenCoords);

        // Debug world coordinates
        System.out.println("[DEBUG_MOUSE] World coordinates: " + mousePos.x + ", " + mousePos.y);
        System.out.println("[DEBUG_MOUSE] Camera position: " + cam.position.x + ", " + cam.position.y + ", zoom: " + cam.zoom);

        return new Vector2(mousePos.x, mousePos.y);
    }

    public void listen() {
        isListening = true;
    }

    public void close() {
        isListening = false;
    }


    public boolean receivedClick(GuiElement target, Vector2 mousePos){
        // Add debug output to verify the bounds are being accessed correctly
        System.out.println("[DEBUG_LISTENER] Target class: " + target.getClass().getSimpleName());
        System.out.println("[DEBUG_LISTENER] Target bounds: " + target.bounds.x + ", " + target.bounds.y + ", " +
                          target.bounds.width + ", " + target.bounds.height);
        System.out.println("[DEBUG_LISTENER] Mouse position: " + mousePos.x + ", " + mousePos.y);

        boolean contains = target.bounds.contains(mousePos.x, mousePos.y);
        System.out.println("[DEBUG_LISTENER] Contains: " + contains);

        return contains;
    }

    /**
     * Checks if a mouse click occurred on the given GUI element.
     * This method should be called every frame to detect clicks.
     *
     * @param target The GUI element to check for clicks
     * @param cam The camera used for coordinate conversion
     * @return true if the element was clicked in this frame, false otherwise
     */
    public boolean isClicked(GuiElement target, OrthographicCamera cam) {
        if (!isListening) return false;

        // Get current mouse position
        Vector2 mousePos = getMousePos(cam);

        // Add debug output for target identification
        System.out.println("[DEBUG_CLICK] Target class: " + target.getClass().getSimpleName());
        System.out.println("[DEBUG_CLICK] Target hash code: " + target.hashCode());

        // Check if mouse is over the element
        boolean isOver = receivedClick(target, mousePos);

        // Check if mouse was just clicked (pressed and released)
        boolean mouseDown = Gdx.input.isTouched();

        // Debug output
        System.out.println("[DEBUG_JAVA] Mouse position: " + mousePos.x + ", " + mousePos.y);
        System.out.println("[DEBUG_JAVA] Button bounds: " + target.bounds.x + ", " + target.bounds.y + ", " +
                          target.bounds.width + ", " + target.bounds.height);
        System.out.println("[DEBUG_JAVA] Is over: " + isOver);
        System.out.println("[DEBUG_JAVA] Mouse down: " + mouseDown);
        System.out.println("[DEBUG_JAVA] Was mouse down: " + wasMouseDown);

        // A click is when the mouse was down in the previous frame and is now up, while over the element
        boolean clicked = !mouseDown && wasMouseDown && isOver;

        System.out.println("[DEBUG_JAVA] Clicked: " + clicked);

        // Update mouse state for next frame
        wasMouseDown = mouseDown;

        return clicked;
    }

    /**
     * Checks if the mouse button is currently pressed down on the given GUI element.
     *
     * @param target The GUI element to check
     * @param cam The camera used for coordinate conversion
     * @return true if the mouse is pressed on the element, false otherwise
     */
    public boolean isPressed(GuiElement target, OrthographicCamera cam) {
        if (!isListening) return false;

        // Get current mouse position
        Vector2 mousePos = getMousePos(cam);

        // Check if mouse is over the element and pressed
        return receivedClick(target, mousePos) && Gdx.input.isTouched();
    }


    public static GuiListener New(){
        return new GuiListener();
    }

}
