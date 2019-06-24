package game_management.game;

import GUI.frames.ChoseFellowScreen;
import GUI.frames.OnlineGameScreen;
import GUI.frames.UserLoginScreen;
import card_management.Card;
import card_management.Deck;
import finals.Message;
import game_management.players.OnlinePlayer;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private ArrayList<String> messagesQue;
    private final  Object lock = new Object();
    private final  Object lock1 = new Object();
    private LocalGame game;
    private  ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean gameRoomReady;
    private boolean isIconVisible;
    private OnlinePlayer onlinePlayer;
    private int counter;
    private Deck deck;
    private GameStatus gameStatus;
    private OnlineGameScreen screen;
    private UserLoginScreen loginScreen;
    private boolean isBettingEnded;
    private boolean isGameEnded;
    private ListenForMessages listenForMessages;
    private ArrayList<String> opponentsNames;
    private HandleMessages handleMessages;

    public Client() {
        resetEnvironment();
    }

    public void startGame() {
        game.goToMenuScreen();
        if (game.isMultiplayer()) {
            runMultiplayerGame();
        }
    }

    private void runMultiplayerGame() {
            try {
                setup();
                bet();
                chooseFellow();
                play();
            } catch (IOException | InterruptedException e) {
                if (e.getMessage().equals("Connection reset")) {
                    resetGame();
                } else {
                    e.printStackTrace();
                }
            }
    }

    private void play() throws InterruptedException, IOException {
        while (!isGameEnded) {
            synchronized (lock1) {
                while (!gameStatus.equals(GameStatus.MY_TURN)) {
                    lock1.wait();
                }
            }
            screen.setCardsEnabled(true);
            screen.setActionListener();
            synchronized (lock) {
                while (screen.isTurnDone()) {
                    lock.wait();
                }
            }
            sendMessage(Message.YOUR_TURN + onlinePlayer.pickACard(screen.getImageString()).getCardId());
            screen.updatePlayerCards(onlinePlayer);
            screen.setTurnDone(false);
            screen.setCardsEnabled(false);
            gameStatus = GameStatus.WAIT;
        }
    }

    private void chooseFellow() throws InterruptedException, IOException {
        synchronized (lock1) {
            while (gameStatus.equals(GameStatus.WAIT)) {
                lock1.wait();
            }
        }
        if (gameStatus.equals(GameStatus.CHOOSE_FELLOW)) {
            screen.setVisible(false);
            ChoseFellowScreen choseFellowScreen = new ChoseFellowScreen(onlinePlayer, lock);
            choseFellowScreen.setVisible(true);
            synchronized (lock) {
                while (choseFellowScreen.isFellowChosen()) {
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
    }

    private void bet() throws InterruptedException, IOException {
        while (!isBettingEnded) {
            synchronized (lock1) {
                while (gameStatus.equals(GameStatus.WAIT)) {
                    lock1.wait();
                }
            }
            if (gameStatus.equals(GameStatus.BETTING)) {
                screen.setBetAreaVisibility(true);
                synchronized (lock) {
                    while (screen.isBetDone()) {
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
            } else if (gameStatus.equals(GameStatus.RUNNING)) {
                isBettingEnded = true;
                gameStatus = GameStatus.WAIT;
            }
        }
    }

    private void setup() throws IOException, InterruptedException {
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
        loginScreen.setLoginPanelVisibility(false);
        if(loginScreen.isSigningUp()) {
            signUp(loginScreen.getUsername(), loginScreen.getPassword(), loginScreen.getIp());
        }
        else {
            login(loginScreen.getUsername(), loginScreen.getPassword(), loginScreen.getIp());
        }
        listenForMessages = new ListenForMessages();
        Thread thread = new Thread(listenForMessages);
        thread.start();
        handleMessages = new HandleMessages();
        Thread thread1 = new Thread(handleMessages);
        thread1.start();

        onlinePlayer = new OnlinePlayer(loginScreen.getUsername());
        synchronized (lock1) {
            while (gameStatus != GameStatus.SETUP) {
                lock1.wait();
            }
        }
        onlinePlayer.sortHand();
        System.out.println(onlinePlayer);
        loginScreen.dispose();
        screen = new OnlineGameScreen(onlinePlayer, lock);
        screen.pack();
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);
        screen.setGameType(GameType.ONLINE);
        screen.setTurnDone(false);
        // waitForMessage();
        isBettingEnded = false;
    }

    private void resetGame() {
        System.out.println("Connection with Server lost, Game Restarting");
        loginScreen.dispose();
        if(gameRoomReady) {
            this.screen.dispose();
        }
        if(handleMessages.isRunning && listenForMessages.isRunning) {
            this.listenForMessages.stop();
            this.handleMessages.stop();
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
        private boolean isRunning = false;
        @Override
        public void run() {
            isRunning = true;
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
        private boolean isRunning = false;

        HandleMessages() {
            this.exit = false;
        }

        @Override
        public void run() {
            isRunning = true;
            while (!exit) {
                if(!messagesQue.isEmpty()) {
                    String message = messagesQue.get(0);
                    String[] details = message.split("&");
                    System.out.println("messageHandled " + message);
                    String control = details[0] + "&";
                    switch (control) {
                        case Message.SIGN_UP:
                            if(details[1].equals("ok")) {
                                String s = "You have correctly signed up! Now you can play Online!";
                                JOptionPane.showMessageDialog(loginScreen.getFrame(),s,"Successfull",JOptionPane.INFORMATION_MESSAGE);
                                resetGame();
                                try {
                                    ois.close();
                                    oos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case Message.ERROR:
                            String s = details[1];
                            JOptionPane.showMessageDialog(loginScreen.getFrame(),s,"Attention",JOptionPane.ERROR_MESSAGE);
                            resetGame();
                            break;
                        case Message.NO_BET:
                            isBettingEnded = true;
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
                            onlinePlayer.draw(convertCard(details[1]));
                            counter++;
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
                        case Message.HIGHER_BET:
                            screen.setHigherBet(Integer.parseInt(details[1]));
                            screen.updateSpinner();
                            break;
                        case  Message.YOUR_TURN:
                            gameStatus = GameStatus.MY_TURN;
                            synchronized (lock1) {
                                lock1.notifyAll();
                            }
                            break;
                        case Message.MY_ICON_TURN:
                            if(!details[1].equals(onlinePlayer.getPlayerName())) {
                                if (!isIconVisible) {
                                    screen.showYourTurn(details[1], true);
                                    isIconVisible = true;
                                } else {
                                    screen.showYourTurn(details[1], false);
                                    isIconVisible = false;
                                }
                            }
                            break;
                        case Message.CALLER:
                            screen.showBriscola(convertCard(details[1]),details[2]);
                            break;
                        case Message.END_OF_GAME:
                            isGameEnded = true;
                            try {
                                sendMessage(Message.END_OF_GAME + onlinePlayer.getPlayerName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            screen.setExitButtonVisibility(true);
                            synchronized (lock) {
                                while (screen.isGameEnded()) {
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


    private Card convertCard(String message) {
        Card card = new Card(message);
        for (Card c:deck.getDeck()) {
            if(c.equals(card)) {
                card.setCardImage(c.getCardImage());
            }
        }
        return card;
    }

    private void login(String name,String password, String ip) throws IOException, InterruptedException {
        createSocket(ip);
        String s = Message.LOGIN + name + "&" + password;
            sendMessage(s);
            Thread.sleep(100);
        }
    private void signUp(String name,String password, String ip) throws IOException, InterruptedException {
        createSocket(ip);
        String s = Message.SIGN_UP + name + "&" + password;
        sendMessage(s);
        Thread.sleep(100);
    }

    private void createSocket(String ip) throws IOException {
        ois = null;
        int port = 9876;
        Socket socket = new Socket(ip, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Sending request to Socket Server");
    }

    private void sendMessage(String name) throws IOException {
        System.out.println("Sending request to Socket Server");
        System.out.println(name);
        oos.writeObject(name);
    }
}
