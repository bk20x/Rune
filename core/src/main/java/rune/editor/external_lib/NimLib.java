package rune.editor.external_lib;

import com.google.gson.JsonObject;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface NimLib extends Library {
    static final String PATH = "C:/Users/Sean/Desktop/bkexedit/dll/parser.dll";

    NimLib Instance = Native.load(PATH, NimLib.class);

    public void helloFromNim();
    void test();
    void writeSaveFile(String data);
}
