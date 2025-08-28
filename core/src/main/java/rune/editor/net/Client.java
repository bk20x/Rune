package rune.editor.net;

import rune.editor.GameScreen;
import rune.editor.Player;
import rune.editor.scene.GameState;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class Client {


    private final String host;
    private final int port;
    private static final int TIMEOUT = 5000;
    public boolean connected = false;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(Player player) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);
            InetAddress address = InetAddress.getByName(host);
            String message = "start!" + player.statusJson();


            byte[] sendBuffer = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(sendPacket);
            System.out.println("Sent: " + message);


            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);


            socket.receive(receivePacket);


            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
            System.out.println("Received from server: " + receivedMessage);
            Thread.sleep(1000);
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: No response received from server within " + TIMEOUT + "ms.");
            connected = false;
            start(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
