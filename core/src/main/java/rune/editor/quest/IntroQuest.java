package rune.editor.quest;

import rune.editor.Player;
import rune.editor.entity.Slime;


import java.util.ArrayList;
import java.util.Vector;



public class IntroQuest extends Quest {

    public ArrayList<Slime> killedSlimes;

    public IntroQuest() {
        super("intro");
        killedSlimes = new ArrayList<>();
    }


    @Override
    public void act(Player player) {
        if(player.killed instanceof Slime){
            Slime killed = ((Slime) player.killed).clone();
            killedSlimes.add(killed);
            player.killed = null;
        }
        if(killedSlimes.size() == 8){
            complete(player);
            killedSlimes.clear();
        }

    }

    @Override
    public void complete(Player player) {
        super.complete(player);
    }

    @Override
    public void giveRewards(Player player) {
        super.giveRewards(player);
    }

}
