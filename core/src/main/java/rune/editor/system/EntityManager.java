package rune.editor.system;

import rune.editor.Player;
import rune.editor.Renderer;
import rune.editor.entity.Entity;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager {

    public final ConcurrentHashMap<Integer, Entity> entities;
    private final ArrayList<Entity> newEnts;

    public EntityManager(){
        entities = new ConcurrentHashMap<>();
        newEnts = new ArrayList<>();
    }

    public void draw(Renderer renderer,float dt){
        for(int i: entities.keySet()){
            entities.get(i).update(dt);
            if(entities.get(i).alive) {
                entities.get(i).draw(renderer,dt);
            }

            if(!entities.get(i).alive){
               entities.get(i).dispose();
               entities.remove(i);
            }
        }
    }


    public void combat(Player player){
        for (int i : entities.keySet()){
            if(!entities.get(i).alive){
                player.lastEntityKilled = entities.get(i);
                entities.remove(i);
                continue;
            }
            if(player.hitMob(entities.get(i)) && entities.get(i).alive){
                entities.get(i).collided(player.wepRec);
                entities.get(i).appliedDamage = player.currentWeapon.damage;
            }
        }
    }


    public void add(Entity e) {
        newEnts.add(e);
        for(Entity ent: newEnts){
            if(entities.containsKey(ent.id)){
                ent.id++;
            }
            entities.put(ent.id,ent);
        }
        newEnts.clear();
    }



    public void removeEntity(int id){
        entities.remove(id);
    }
    public void getEntity(int id){
        entities.get(id);
    }


    public int size(){
        return entities.size();
    }
}
