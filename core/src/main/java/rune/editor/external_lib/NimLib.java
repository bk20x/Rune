package rune.editor.external_lib;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface NimLib extends Library {
    String PATH = "C:\\Users\\Droop\\Desktop\\Hreload\\RuneGameh\\Rune\\dll\\preload.dll";

    NimLib Instance = Native.load(PATH, NimLib.class);
    void libraryInit();

    String queryMob(String elementName);
    String queryItem(String elementName);
    void test();

}
