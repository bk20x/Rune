package rune.editor.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rune.editor.GameScreen;
import rune.editor.Player;
import rune.editor.scene.GameState;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {


    private final String host;
    private final int port;
    private static final int TIMEOUT = 150;
    public boolean connected = false;
    public boolean shouldRun = false;
    private final DatagramSocket socket;
    private final Gson gson = new Gson();
    private Thread clientThread;
    public volatile boolean clientThreadRunning = false;
    private volatile String lastMessage;
    public CLIENT_MODE mode;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.shouldRun = true;
        mode = CLIENT_MODE.JOINING;
        try {
            socket = new DatagramSocket(6767);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    boolean started = false;


    public void start(Player player) {
        if (player == null) return;


        try {
            socket.setSoTimeout(TIMEOUT);

            DatagramPacket sendPacket = switch (mode){
                case JOINING -> sendJoinRequest(player);
                case PEERING, IN_SESSION ->  updateStatus(player);
            };

            socket.send(sendPacket);
            System.out.println("Sent " + new String(sendPacket.getData()));


            try {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                connected = true;

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                lastMessage = receivedMessage;


                if(mode == CLIENT_MODE.PEERING) {
                    try {
                        var json = gson.fromJson(receivedMessage, JsonObject.class);

                        if (json != null) {
                            if (json.get("player") != null) {
                                Player peer = new Player();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("error parsing json " + receivedMessage);
                    }
                    System.out.println("Received from server: " + receivedMessage);
                }
                if(mode == CLIENT_MODE.JOINING && receivedMessage.contains("peering started")) {
                    mode = CLIENT_MODE.PEERING;
                }
            } catch (SocketTimeoutException e) {
                System.err.println("Timeout: No response received from server within " + TIMEOUT + "ms.");
                connected = false;
            }
        } catch (Exception e) {
            System.err.println("Error in network thread: " + e.getMessage());
        }
    }

    public DatagramPacket sendJoinRequest(Player player) throws IOException {
        String message = "join!" + player.statusJson();
        byte[] sendBuffer = message.getBytes(StandardCharsets.UTF_8);
        InetAddress address = InetAddress.getByName(host);
        return new DatagramPacket(sendBuffer, sendBuffer.length,address, port);
    }
    public DatagramPacket updateStatus(Player player) throws IOException {
        String message = "" + player.statusJson();
        byte[] sendBuffer = message.getBytes(StandardCharsets.UTF_8);
        InetAddress address = InetAddress.getByName(host);
        return new DatagramPacket(sendBuffer, sendBuffer.length,address, port);
    }

    public void stop() {
        shouldRun = false;
        clientThreadRunning = false;
        if (clientThread != null && clientThread.isAlive()) {
            clientThread.interrupt();
            try {
                clientThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public synchronized void startClientThread(Player player) {
        if (clientThreadRunning && clientThread != null && clientThread.isAlive()) {
            return;
        }
        clientThreadRunning = true;
        clientThread = new Thread(() -> {
            try {
                while (shouldRun && clientThreadRunning) {
                    if (player != null) {
                        start(player);
                    }
                    try {
                        Thread.sleep(10); //Up to 100 packets/s
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } finally {
                clientThreadRunning = false;
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
    }

    public String getLastMessage(){
        return lastMessage;
    }

}
