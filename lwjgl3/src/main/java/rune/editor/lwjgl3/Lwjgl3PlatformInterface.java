package rune.editor.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import rune.editor.platform.PlatformInterface;

/**
 * LWJGL3-specific implementation of the PlatformInterface.
 */
public class Lwjgl3PlatformInterface implements PlatformInterface {

    @Override
    public long getWindowHandle() {
        return ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
    }
}
