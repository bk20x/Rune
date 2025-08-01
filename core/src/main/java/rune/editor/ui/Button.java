package rune.editor.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import rune.editor.Renderer;

public class Button extends GuiElement {

    public Texture texture;
    public Vector2 pos;

    // Track button state
    private boolean isClicked = false;
    private boolean wasClicked = false;

    public Button(String style, float x, float y) {
        // Call the parent constructor to initialize the bounds field
        super();

        System.out.println("[DEBUG_BUTTON_INIT] Creating button with style: " + style + " at position: " + x + ", " + y);
        System.out.println("[DEBUG_BUTTON_INIT] Initial bounds: " + bounds.x + ", " + bounds.y + ", " +
                          bounds.width + ", " + bounds.height);

        // Check if style already includes file extension
        if (!style.endsWith(".png")) {
            style = style + ".png";
        }
        texture = new Texture("ui/" + style);
        pos = new Vector2(x, y);

        // Initialize the inherited bounds field
        bounds.x = x;
        bounds.y = y;
        bounds.width = texture.getWidth();
        bounds.height = texture.getHeight();

        System.out.println("[DEBUG_BUTTON_INIT] Final bounds: " + bounds.x + ", " + bounds.y + ", " +
                          bounds.width + ", " + bounds.height);
        System.out.println("[DEBUG_BUTTON_INIT] Button hash code: " + this.hashCode());
    }

    @Override
    public void draw(Renderer renderer) {
        // Draw the button texture at its position
        renderer.sb.draw(texture, pos.x, pos.y);

        // Debug output to verify button position and bounds
        System.out.println("[DEBUG_BUTTON] Drawing button at: " + pos.x + ", " + pos.y);
        System.out.println("[DEBUG_BUTTON] Button bounds: " + bounds.x + ", " + bounds.y + ", " +
                          bounds.width + ", " + bounds.height);
    }

    /**
     * Checks if the button was clicked using the provided GuiListener
     *
     * @param listener The GuiListener to use for input detection
     * @param camera The camera for coordinate conversion
     * @return true if the button was clicked in this frame
     */
    public boolean checkClicked(GuiListener listener, OrthographicCamera camera) {
        if (listener == null) return false;

        // Make sure the listener is active
        if (!listener.isListening) {
            listener.listen();
        }

        // Check if button was clicked
        isClicked = listener.isClicked(this, camera);

        // Store the result to allow wasClicked() to work
        if (isClicked) {
            wasClicked = true;
        }

        return isClicked;
    }

    /**
     * Returns true if the button was clicked since the last call to this method
     * This is useful for Lua code that needs to check if a button was clicked
     *
     * @return true if the button was clicked, and resets the clicked state
     */
    public boolean wasClicked() {
        boolean result = wasClicked;
        wasClicked = false;
        return result;
    }

    /**
     * Sets the position of this button
     *
     * @param x The new x position
     * @param y The new y position
     * @return This button for method chaining
     */
    public Button setPosition(float x, float y) {
        pos.x = x;
        pos.y = y;
        bounds.x = x;
        bounds.y = y;

        // Add debug output to verify bounds are being updated
        System.out.println("[DEBUG_BUTTON] Updated position to: " + x + ", " + y);
        System.out.println("[DEBUG_BUTTON] Updated bounds to: " + bounds.x + ", " + bounds.y + ", " +
                          bounds.width + ", " + bounds.height);
        return this;
    }

    /**
     * Factory method to create a new Button
     */
    public static Button New(String style, float x, float y) {
        return new Button(style, x, y);
    }
}
