package rune.editor.net;

import rune.editor.Player;

import java.util.ArrayList;

public class Session {


    public Client client;
    public Player peer;
    public boolean peering = false;
    public boolean foundPeer = false;

    public Session(String host, int port) {
        client = new Client(host, port);

    }

    public void start(Player player) {
        client.startClientThread(player);
    }

    public void run(){
        if(client.getLastMessage() != null){
            if(client.getLastMessage().equalsIgnoreCase("")){

            }
        }


    }
    public void createPeer(){
        if(peering){
            try {

            }catch (Exception e){
                System.err.println("Error attempting to create peer: " + e.getMessage());
            }
        }
    }
}
