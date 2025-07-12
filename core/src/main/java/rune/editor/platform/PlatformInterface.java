package rune.editor.platform;

/**
 * Interface for platform-specific operations.
 * This allows the core module to remain platform-independent while still
 * accessing platform-specific functionality through implementations provided
 * by platform-specific modules.
 */
public interface PlatformInterface {
    /**
     * Gets the window handle for ImGui initialization.
     * @return The window handle as a long value.
     */
    long getWindowHandle();
}
