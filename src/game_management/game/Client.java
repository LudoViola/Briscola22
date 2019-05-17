package game_management.game;

import GUI.frames.GameScreen;
import GUI.frames.OnlineGameScreen;
import GUI.frames.UserLoginScreen;
import card_management.Card;
import card_management.Deck;
import finals.Message;
import game_management.players.OnlinePlayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final  Object lock = new Object();
    private LocalGame game;
    private  String name;
    private  ObjectInputStream ois;
    private  Socket socket;
    private final  int port = 9876;
    private boolean gameRoomReady;
    private OnlinePlayer onlinePlayer;
    private int counter;
    private Deck deck;

    public Client() {
        game = new LocalGame(lock);
        gameRoomReady = false;
        counter = 0;
        deck = new Deck();
    }

    public void startGame() {
        game.goToMenuScreen();
        if (game.isMultiplayer()) {
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
                onlinePlayer = new OnlinePlayer(name);
                waitForGameReady();
                onlinePlayer.sortHand();
                System.out.println(onlinePlayer);
                OnlineGameScreen screen = new OnlineGameScreen(onlinePlayer,lock);
                screen.pack();
                screen.setLocationRelativeTo(null);
                screen.setVisible(true);
                screen.setGameType(GameType.ONLINE);
                screen.setTurnDone(false);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                if(e.getMessage().equals("Connection reset")) {
                    resetGame(loginScreen);
                }
                else {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetGame(UserLoginScreen loginScreen) {
        System.out.println("Connection with Server lost, Game Restarting");
        loginScreen.dispose();
        game = new LocalGame(lock);
        startGame();
    }

    private  void waitForGameReady()throws IOException, ClassNotFoundException {
        boolean check = true;
            while (check) {
                if (socket.isConnected()) {
                    ois = new ObjectInputStream(socket.getInputStream());
                    String message = (String) ois.readObject();
                    if(!gameRoomReady) {
                        if (Message.SENDING_CARD.equals(message)) {
                            System.out.println("Receiving cards");
                            gameRoomReady = true;
                        }
                    }
                    else {
                        Card card = new Card(message);
                        for (Card c:deck.getDeck()) {
                            if(c.equals(card)) {
                                card.setCardImage(c.getCardImage());
                            }
                        }
                        onlinePlayer.draw(card);
                        counter++;
                        if(counter == 8) {
                            break;
                        }
                    }
                }
            }
        }

        private void login(String name) throws IOException, ClassNotFoundException, InterruptedException {
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

                System.out.println("Message: " + message);
            //close resources
            //ois.close();
            //oos.close();
            Thread.sleep(100);

        }
}
