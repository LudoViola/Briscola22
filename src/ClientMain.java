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
            loginScreen.getFrame().dispose();

                try {
                    runClient(name);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    private static void runClient(String name) throws IOException, ClassNotFoundException, InterruptedException {
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            oos.writeObject(name);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
    }
}
