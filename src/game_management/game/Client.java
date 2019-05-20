package game_management.game;

import GUI.frames.ChoseFellowScreen;
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
import java.util.ArrayList;

public class Client {
    private ArrayList<String> messagesQue;
    private final  Object lock = new Object();
    private final  Object lock1 = new Object();
    private LocalGame game;
    private  String name;
    private  ObjectInputStream ois;
    private  Socket socket;
    private ObjectOutputStream oos;
    private final  int port = 9876;
    private boolean gameRoomReady;
    private boolean isIconVisible;
    private OnlinePlayer onlinePlayer;
    private int counter;
    private Deck deck;
    private GameStatus gameStatus;
    private OnlineGameScreen screen;
    private UserLoginScreen loginScreen;
    private boolean check22;
    private boolean isGameEnded;
    private Thread thread;
    private ListenForMessages listenForMessages;
    private ArrayList<String> opponentsNames;
    private HandleMessages handleMessages;

    public Client() {
        resetEnvironment();
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
                listenForMessages = new ListenForMessages();
                thread = new Thread(listenForMessages);
                thread.start();
                handleMessages = new HandleMessages();
                Thread thread1 = new Thread(handleMessages);
                thread1.start();
                synchronized (lock1) {
                    while (gameStatus!=GameStatus.SETUP) {
                        lock1.wait();
                    }
                }
                onlinePlayer.sortHand();
                System.out.println(onlinePlayer);
                loginScreen.dispose();
                screen = new OnlineGameScreen(onlinePlayer,lock);
                screen.pack();
                screen.setLocationRelativeTo(null);
                screen.setVisible(true);
                screen.setGameType(GameType.ONLINE);
                screen.setTurnDone(false);
               // waitForMessage();
                check22= false;
                while (!check22) {
                    synchronized (lock1) {
                        while (gameStatus.equals(GameStatus.WAIT)) {
                            lock1.wait();
                        }
                    }
                    if(gameStatus.equals(GameStatus.BETTING)) {
                        screen.setBetAreaVisibility(true);
                        synchronized (lock) {
                            while (!screen.isBetDone()) {
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        screen.setBetDone(false);
                        String bet = Message.SENDING_BET + screen.getBet();
                        screen.setBetAreaVisibility(false);
                        sendMessage(bet);
                        gameStatus = GameStatus.WAIT;
                    }
                    else if(gameStatus.equals(GameStatus.RUNNING)) {
                        check22 = true;
                        System.out.println("hrrh");
                        gameStatus = GameStatus.WAIT;
                    }
                }
                synchronized (lock1) {
                    while (gameStatus.equals(GameStatus.WAIT)) {
                        lock1.wait();
                    }
                }
                if(gameStatus.equals(GameStatus.CHOOSE_FELLOW)) {
                    screen.setVisible(false);
                    ChoseFellowScreen choseFellowScreen = new ChoseFellowScreen(onlinePlayer,lock);
                    choseFellowScreen.setVisible(true);
                    synchronized (lock) {
                        while (!choseFellowScreen.isFellowChosen()) {
                            lock.wait();
                        }
                    }
                    String cardChosen = Message.CHOOSE_YOUR_FELLOW + choseFellowScreen.getCardChosen().getCardId();
                    sendMessage(cardChosen);
                    screen.setVisible(true);
                }
                screen.addNameOnIcons(opponentsNames);
                screen.setTableVisibility(true);
                screen.update(screen.getGraphics());
                screen.revalidate();
                screen.repaint();

                while (!isGameEnded) {
                    screen.removeActionListener();
                    synchronized (lock1) {
                        while (!gameStatus.equals(GameStatus.MY_TURN)) {
                            lock1.wait();
                        }
                    }
                    sendMessage(Message.MY_ICON_TURN+onlinePlayer.getPlayerName());
                    screen.setActionListener();
                    synchronized (lock) {
                        while (!screen.isTurnDone()) {
                            lock.wait();
                        }
                    }
                    sendMessage(Message.YOUR_TURN+onlinePlayer.pickACard(screen.getImageString()).getCardId());
                    screen.updatePlayerCards(onlinePlayer);
                    screen.removeActionListener();
                    screen.setTurnDone(false);
                    gameStatus = GameStatus.WAIT;
                }


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
            this.listenForMessages.stop();
            this.handleMessages.stop();
            this.screen.dispose();
        }
        resetEnvironment();
        startGame();
    }

    private void resetEnvironment() {
        messagesQue = new ArrayList<>();
        opponentsNames = new ArrayList<>();
        game = new LocalGame(lock);
        gameRoomReady = false;
        isGameEnded = false;
        isIconVisible = false;
        counter = 0;
        deck = new Deck();
        gameStatus = GameStatus.WAIT;
    }

    private class ListenForMessages implements Runnable {
        private volatile boolean exit;
        @Override
        public void run() {
            try {
                while (!exit) {
                    String message;
                    message = (String) ois.readObject();
                    System.out.println("messageReceived " + message);
                    messagesQue.add(message);
                }
            }catch (IOException | ClassNotFoundException e) {
                    if(e.getMessage().equals("Connection reset")) {
                        resetGame();
                    }
                    else {
                        e.printStackTrace();
                    }
            }
        }
        void stop() {
            exit = true;
        }
    }

    private class HandleMessages implements Runnable {
        private volatile boolean exit;
        private String playingUser;

        public HandleMessages() {
            this.exit = false;
        }

        @Override
        public void run() {
            while (!exit) {
                if(!messagesQue.isEmpty()) {
                    String message = messagesQue.get(0);
                    String[] details = message.split("&");
                    System.out.println("messageHandled " + message);
                    String control = details[0] + "&";
                    switch (control) {
                        case Message.NO_BET:
                            check22 = true;
                            gameStatus = GameStatus.RUNNING;
                            synchronized (lock1) {
                                lock1.notifyAll();
                            }
                            break;
                        case Message.CHOOSE_YOUR_FELLOW:
                            gameStatus = GameStatus.CHOOSE_FELLOW;
                            synchronized (lock1) {
                                lock1.notifyAll();
                            }
                            break;
                        case Message.SENDING_CARD:
                            gameRoomReady = true;
                            drawCard(details[1]);
                            if (counter == 8) {
                                gameStatus = GameStatus.SETUP;
                                synchronized (lock1) {
                                    lock1.notifyAll();
                                }
                            }
                            break;
                        case Message.YOUR_BETTING_TURN:
                            gameStatus = GameStatus.BETTING;
                            synchronized (lock1) {
                                lock1.notifyAll();
                            }
                            break;
                        case Message.SENDING_NAME:
                            opponentsNames.add(details[1]);
                            break;
                        case Message.LOG:
                            if(details[1].equals("hand")) {
                                screen.logHandWinner(details[2]);
                            }
                            else {
                                screen.log(details[1]);
                            }
                            break;
                        case Message.UPDATE:
                            deck = new Deck();
                            Card card = new Card(details[1]);
                            for (Card c:deck.getDeck()) {
                                if(c.equals(card)) {
                                    card.setCardImage(c.getCardImage());
                                }
                            }
                            screen.updateTableCards(card);
                            break;
                        case  Message.YOUR_TURN:
                            gameStatus = GameStatus.MY_TURN;
                            synchronized (lock1) {
                                lock1.notifyAll();
                            }
                            break;
                        case Message.MY_ICON_TURN:
                            if(!isIconVisible) {
                                screen.showYourTurn(details[1], true);
                                isIconVisible = true;
                                playingUser = details[1];
                            }else {
                                screen.showYourTurn(playingUser, false);
                                isIconVisible = false;
                            }
                            break;
                        case Message.END_OF_GAME:
                            isGameEnded = true;
                            screen.setExitButtonVisibility(true);
                            synchronized (lock) {
                                while (!screen.isGameEnded()) {
                                    try {
                                        lock.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                                screen.setVisible(false);
                                resetGame();
                                break;
                        default:
                            break;
                    }
                    messagesQue.remove(0);
                }
            }
        }
        void stop() {
            exit = true;
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
        InetAddress host = InetAddress.getLocalHost();
        //establish socket connection to server
        socket = new Socket(host.getHostName(), port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Sending request to Socket Server");
        //write to socket using ObjectOutputStream
            sendMessage(name);
            Thread.sleep(100);

        }

    private void sendMessage(String name) throws IOException {
        System.out.println("Sending request to Socket Server");
        System.out.println(name);
        oos.writeObject(name);
    }
}
