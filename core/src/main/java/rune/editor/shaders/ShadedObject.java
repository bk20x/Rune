package rune.editor.shaders;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import rune.editor.Globals;

public class ShadedObject {




    public Texture texture;
    public float[] vertices;
    public FrameBuffer frameBuf;


    public ShadedObject(String name) {
        texture = new Texture(Globals.objectAssetPath + name + ".png");
        vertices = new float[16];
        frameBuf = new FrameBuffer(Pixmap.Format.RGBA8888, texture.getWidth(),texture.getHeight(), false);
    }







}
