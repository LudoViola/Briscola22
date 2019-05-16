import game_management.game.Client;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientMain {
    private final static Object lock = new Object();
    private static String name;
    private static boolean isGameRoomReady = false;
    private static ObjectInputStream ois;
    private static Socket socket;
    private final static int port = 9876;

    public static void main(String[] args) {
        Client user = new Client();
        user.startGame();
    }
}
