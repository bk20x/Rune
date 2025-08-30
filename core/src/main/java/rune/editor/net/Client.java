package rune.editor.net;

import rune.editor.GameScreen;
import rune.editor.Player;
import rune.editor.scene.GameState;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client {


    private final String host;
    private final int port;
    private static final int TIMEOUT = 150;
    public boolean connected = false;
    public boolean shouldRun;
    private final DatagramSocket socket;
    private Thread clientThread;
    public volatile boolean clientThreadRunning = false;
    private volatile String lastMessage;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.shouldRun = true;
        try {
            socket = new DatagramSocket(6767);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    boolean started = false;


    public void start(Player player) {
        if (player == null) {
            return;
        }

        try {
            socket.setSoTimeout(TIMEOUT);

            String message = "" + player.statusJson();
            byte[] sendBuffer = message.getBytes(StandardCharsets.UTF_8);


            InetAddress address = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(sendPacket);
            System.out.println("Sent " + message);

            try {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                connected = true;

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                lastMessage = receivedMessage;
                System.out.println("Received from server: " + receivedMessage);


            } catch (SocketTimeoutException e) {
                System.err.println("Timeout: No response received from server within " + TIMEOUT + "ms.");
                connected = false;
            }
        } catch (Exception e) {
            System.err.println("Error in network thread: " + e.getMessage());
        }
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
