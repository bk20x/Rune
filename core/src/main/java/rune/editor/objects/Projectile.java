package rune.editor.objects;

import com.badlogic.gdx.math.Vector2;

public class Projectile {

    public Vector2 start,pos,vel;
    public float travelSpeed,angle;

    public Projectile(){

    }

    public Projectile(Vector2 src,float angle,float speed){
        this.start = src;
        this.pos = src;
        this.angle = angle;
        this.travelSpeed = speed;
        vel = new Vector2();
    }

    public void update(float dt){
        pos.x += travelSpeed *dt;
    }



}
