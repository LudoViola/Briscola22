import GUI.frames.UserLoginScreen;
import game_management.LocalGame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientMain {
    private final static Object lock = new Object();
    private static String name;
    private static boolean isGameRoomReady = false;
    private static ObjectInputStream ois;
    private static Socket socket;
    private final static int port = 9876;

    public static void main(String[] args) {

        LocalGame game = new LocalGame(lock);
        game.goToMenuScreen();
        if(game.isMultiplayer()) {
            UserLoginScreen loginScreen = new UserLoginScreen(lock);
            loginScreen.getFrame().setVisible(true);
            synchronized (lock) {
                while (!loginScreen.isLogged()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            name = loginScreen.getUsername();
            loginScreen.setLoginPanelVisibility(false);

                try {
                    login(name);
                    waitForGameReady();
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
/*
            synchronized (lock) {
                while (!isGameRoomReady) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }
    }

    private static void waitForGameReady() throws IOException, ClassNotFoundException {
        while (true) {

            if(socket.isConnected()) {
                ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                if (message.equals("ciaone")) {
                    System.out.println(message);
                    break;
                }
            }
        }
    }

    private static void login(String name) throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress host = InetAddress.getLocalHost();
        socket = null;
        ObjectOutputStream oos = null;
        ois = null;
            //establish socket connection to server
            socket = new Socket(host.getHostName(), port);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            oos.writeObject(name);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            if(message.equals("ready")) {
                System.out.println("ready");
            }
            else {
                System.out.println("Message: " + message);
            }
            //close resources
            //ois.close();
            //oos.close();
            Thread.sleep(100);

    }
}
