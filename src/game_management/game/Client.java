package game_management.game;

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
    private GameStatus gameStatus;
    private OnlineGameScreen screen;
    private UserLoginScreen loginScreen;

    public Client() {
        game = new LocalGame(lock);
        gameRoomReady = false;
        counter = 0;
        deck = new Deck();
        gameStatus = GameStatus.SETUP;
    }

    public void startGame() {
        game.goToMenuScreen();
        if (game.isMultiplayer()) {
            loginScreen = new UserLoginScreen(lock);
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
                waitForMessage();
                onlinePlayer.sortHand();
                System.out.println(onlinePlayer);
                loginScreen.dispose();
                screen = new OnlineGameScreen(onlinePlayer,lock);
                screen.pack();
                screen.setLocationRelativeTo(null);
                screen.setVisible(true);
                screen.setGameType(GameType.ONLINE);
                screen.setTurnDone(false);
                waitForMessage();
                System.out.println("here5");
               // waitForMessage();
                screen.dispose();


            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                if(e.getMessage().equals("Connection reset")) {
                    resetGame();
                }
                else {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetGame() {
        System.out.println("Connection with Server lost, Game Restarting");
        loginScreen.dispose();
        if(gameRoomReady) {
            this.screen.dispose();
        }
        game = new LocalGame(lock);
        startGame();
    }

    private  void waitForMessage()throws IOException, ClassNotFoundException {
        boolean check = true;
            while (check) {
                    ois = new ObjectInputStream(socket.getInputStream());
                    String message = (String) ois.readObject();
                    System.out.println(message);
                    switch (message) {
                        case Message.NO_BET:
                            gameStatus = GameStatus.RUNNING;
                            System.out.println("here1");
                            check = false;
                            break;
                        case Message.YOUR_TURN:
                            System.out.println("here");
                            check = false;
                            break;
                        case Message.SENDING_CARD:
                            System.out.println("Receiving cards");
                            gameRoomReady = true;
                            break;
                        case Message.YOUR_BETTING_TURN:
                            check = false;
                            screen.revalidate();
                            screen.repaint();
                            gameStatus = GameStatus.BETTING;
                            screen.setBetAreaVisibility(true);
                            System.out.println("sono qui");
                            synchronized (lock) {
                                while (!screen.isBetDone()) {
                                    try {
                                        lock.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            String bet = "" + screen.getBet();
                            sendMessage(Message.SENDING_BET);
                            screen.setBetAreaVisibility(false);
                            sendMessage(bet);
                            break;
                        default:
                            if (gameStatus == GameStatus.SETUP) {
                                drawCard(message);
                                if (counter == 8) {
                                    check = false;
                                }
                            }
                            break;
                    }
                }
                }

    private void drawCard(String message) {
        Card card = new Card(message);
        for (Card c:deck.getDeck()) {
            if(c.equals(card)) {
                card.setCardImage(c.getCardImage());
            }
        }
        onlinePlayer.draw(card);
        counter++;
    }

    private void login(String name) throws IOException, ClassNotFoundException, InterruptedException {
            socket = null;
            ois = null;
        sendMessage(name);
        //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            //ois.close();
            //oos.close();
            Thread.sleep(100);

        }

    private void sendMessage(String name) throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        //establish socket connection to server
        socket = new Socket(host.getHostName(), port);
        //write to socket using ObjectOutputStream
        ObjectOutputStream oos;
        oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Sending request to Socket Server");
        System.out.println(name);
        oos.writeObject(name);
    }
}
