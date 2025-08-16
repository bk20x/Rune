package rune.editor.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.luaj.vm2.ast.Str;
import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;
import rune.editor.gui.Button;
import rune.editor.gui.GuiLayout;
import rune.editor.magic.Spell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static rune.editor.data.GameData.writePlayerSaveFile;


public class GameState {


    public static String activeQuest;

    public static void setActiveQuest(String questName) {
        activeQuest = questName;
    }

    public Scene scene;
    public Player player;
    public final Renderer renderer;
    public boolean isSceneActive = true;
    public OrthographicCamera camera;
    public SceneTransitionManager transitionManager;

    public GuiLayout guiLayout;
    public Button button;
    public float stateTime;

    ShaderProgram activeShader;
    String vert,frag;


    public GameState() {
        renderer = new Renderer();
        guiLayout = new GuiLayout();
        transitionManager = new SceneTransitionManager();

        vert = Gdx.files.internal("vert.glsl").readString();
        frag = Gdx.files.internal("bloodred_fade1.glsl").readString();
        activeShader = new ShaderProgram(vert, frag);
    }

    public void addEntity(Entity e) {
        scene.entityManager.add(e);
    }

    public void addPlayer(Player player) {
        this.player = player;
    }


    public synchronized void setScene(Scene scene) {
        isSceneActive = false;
        renderer.flush();
        if (player != null) {
            player.lastEntityKilled = null;
        }
        if (this.scene != null) {
            this.scene.dispose();
        }
        this.scene = scene;
        isSceneActive = true;
    }

    public void transitionToScene(Scene targetScene, String exitDirection) {
        if (!transitionManager.isTransitioning()) {
            transitionManager.startTransition(targetScene, exitDirection);
        }
    }



    public void setTint(Color color) {
        renderer.sb.setColor(color);
        scene.mapRenderer.getBatch().setColor(color);
    }

    public void setView(OrthographicCamera camera) {
        if (this.camera == null) {
            this.camera = camera;
        }
        scene.setView(this.camera);
        renderer.setView(this.camera);
    }


    public Vector2 getMousePos() {
        Vector3 screenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 unprojected = camera.unproject(screenCoords);
        return new Vector2(unprojected.x, unprojected.y);
    }




    public void run(Renderer renderer, float dt) {
        stateTime += dt;
        boolean transitionComplete = transitionManager.updateTransitionState(dt, player, this);

        renderer.start();
        applyShader();
        if (isSceneActive && scene != null && player != null && camera != null) {
            setView(camera);

            scene.update();
            scene.draw(renderer, dt);
            scene.playerInteract(player);

            if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
                writePlayerSaveFile(player);
            }

            if (player.isMelee) {
                scene.entityManager.combat(player);
            }


            if (transitionComplete) {
                player.draw(renderer, dt);
            }

            if (!transitionManager.isTransitioning()) {
                checkSceneTransitions();
            }
            for (int i : scene.entityManager.entities.keySet()) {
                scene.entityManager.entities.get(i).followPlayer(player, dt);
            }
        }
        transitionManager.renderTransitionEffect();
        resetShaderToDefault();
        renderer.stop();
    }




    private void checkSceneTransitions() {
        if (player == null || scene == null) return;
        if (player.pos.x < 64 && player.isMoving &&
            player.direction == rune.editor.types.DIRECTION.WEST) {

            Scene newScene = new Scene();
            transitionToScene(newScene, "left");
        }
    }

    public static GameState New() {
        return new GameState();
    }


    public void setActiveShader(ShaderProgram activeShader) {
        if(this.activeShader != null) {
            this.activeShader.dispose();
            this.activeShader = null;
        }
        this.activeShader = activeShader;
    }
    public void setActiveShader(String vertex, String fragment) {
        String vertexShader = Gdx.files.internal(vertex).readString();
        String fragmentShader = Gdx.files.internal(fragment).readString();
        setActiveShader(new ShaderProgram(vertexShader, fragmentShader));
    }


    public void setActiveShaderDbg(String vertPath, String fragPath) {
        String vertShader = null;
        String fragShader = null;
        try {
            vertShader = new String(Files.readAllBytes(Path.of( "C:\\Users\\Sean\\Desktop\\RuneGameh\\Rune\\assets\\"+vertPath)));
            fragShader = new String(Files.readAllBytes(Path.of("C:\\Users\\Sean\\Desktop\\RuneGameh\\Rune\\assets\\" + fragPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setActiveShader(new ShaderProgram(vertShader, fragShader));
    }

    public void resetShaderToDefault() {
        renderer.sb.setShader(null);
        scene.mapRenderer.getBatch().setShader(null);
    }

    public void applyShader() {
        if (activeShader != null) {
            stateTime += Gdx.graphics.getDeltaTime();
            activeShader.bind();
            activeShader.setUniformf("u_time", stateTime);
            renderer.sb.setShader(activeShader);
            scene.mapRenderer.getBatch().setShader(activeShader);
        }
    }

    public ShaderProgram getActiveShader() {
        return activeShader;
    }

}
