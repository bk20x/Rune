package rune.editor.entity;

import com.badlogic.gdx.math.Circle;
import rune.editor.Player;
import rune.editor.types.DIRECTION;

public interface MobIface {


    void follow(Entity mob,Player player, float dt);
    void attack(Entity mob,Player player);




}
