package rune.editor.lua;



import party.iroiro.luajava.ExternalLoader;
import party.iroiro.luajava.Lua;
import party.iroiro.luajava.lua54.Lua54;
import rune.editor.entity.Entity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Interop {


    private final ExternalLoader loader;
    private final Lua lua = new Lua54();

    public Interop() {
         loader = (name, lua) -> {
             try {
                 String filePath = "lua/" + name + ".lua";
                 byte[] bytes = Files.readAllBytes(Paths.get(filePath));
                 ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
                 buffer.put(bytes);
                 buffer.flip();
                 return buffer;
             } catch (IOException e) {
                 return null;
             }
         };

        lua.openLibraries();
        lua.setExternalLoader(loader);
    }


    public void require(String modName){
        loader.load(modName,lua);
    }

    public void loadLuaFile(String filePath) {

        require("lava");
        try {
            String luaScript = new String(Files.readAllBytes(Paths.get(filePath)));
            lua.load(luaScript);
            lua.pCall(0, 0);
        } catch (IOException e) {
            throw new RuntimeException("Error loading script: " + e.getMessage());
        }
    }


    public void callLuaFunc(String funcName) {
        lua.getGlobal(funcName);
        lua.pCall(0, 0);
    }


    public void dispose() {
        if (lua != null) {
            lua.close();
        }
    }







}
