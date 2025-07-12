package rune.editor.objects;

import rune.editor.entity.Entity;
import rune.editor.entity.Slime;
import rune.editor.types.EntityTypes;

public class Mobs {


    public static Entity OrangeSlime(){return Slime.Orange();}
    public static Entity GreenSlime(){return Slime.Green();}
    public static Entity BlueSlime(){return Slime.Blue();}

    public static Entity Skeleton(){return Entity.mob("skeleton");}

    public static Entity New(String name){return new Entity(name,EntityTypes.MOB);}


}
