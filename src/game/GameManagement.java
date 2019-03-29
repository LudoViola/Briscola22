package game;

import GUI.frames.ChoseFellowScreen;
import GUI.frames.GameScreen;
import GUI.frames.NewGameScreen;
import card_management.Card;
import card_management.Deck;
import card_management.Semi;
import game.players.AIPlayer;
import game.players.AIPlayerRandom;
import game.players.ControlledPlayer;
import game.players.Player;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class GameManagement {
    private ArrayList<Player> players;
    private ArrayList<Player> teamCaller;
    private ArrayList<Player> teamPopolo;
    private Deck deck;
    private Semi briscola;
    private Card fellowCard;
    private int startingPlayer;
    private int higherBet;
    private CopyOnWriteArrayList<Player> bettingPlayers;
    private boolean betAnswer;
    private GameScreen screen;
    private final Object lock;
    private boolean isFirst;
    private GameType gameType;

    public GameManagement() {
        this.lock = new Object();
    }

    private void resetGameEnvironment() {
        higherBet = 60;
        startingPlayer = 0;
        isFirst = true;
        betAnswer = false;
        this.players = new ArrayList<>();
        generatePlayers();
        this.deck = new Deck();
        this.deck.shuffle();
        distributeCard();
        this.screen = new GameScreen(players.get(0), lock);
        bettingPlayers = new CopyOnWriteArrayList<>(players);
        this.screen.setVisible(true);

        this.screen.setTurnDone(false);
    }

    public void startGame() {
        goToMenuScreen();
        bettingTurn();
        chooseFellow();
        playPhase();
        makeScoreBoard();
    }

    private void bettingTurn() {
        while (bettingPlayers.size()!=1) {
            for (Player p : bettingPlayers) {
                System.out.println(bettingPlayers);
                int bet = 0;
                p.sortHand();
                switchScreen(p);
                if (!(bettingPlayers.contains(p) && bettingPlayers.size() == 1)) {
                    if(p instanceof ControlledPlayer) {
                        synchronized (lock) {
                            while (!screen.isBetDone()) {
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        bet = screen.getBet();
                    }
                    else if(p instanceof AIPlayer) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        AIPlayerRandom playerRandom = (AIPlayerRandom) p;
                        int temp = playerRandom.chooseBet();
                        while (!(temp<121 && temp> higherBet || temp == 0)) {
                            temp = playerRandom.chooseBet();
                        }
                        bet = temp;
                    }

                    if (bet > 60 && bet < 121) {
                        if (bet > higherBet) {
                            higherBet = bet;
                            screen.setHigherBet(higherBet);
                            startingPlayer = p.getOrder();
                            screen.displayBettingMove(p,bet);
                        }
                    } else if (bet == 0) {
                        screen.displayBettingMove(p,bet);
                        bettingPlayers.remove(p);
                    }
                    screen.setBetDone(false);
                    isFirst = false;
                }
            }
        }
        if(higherBet == 60) {
            String b = "Start a new game";
            screen.diplayBettingWinner(b);
            resetGameEnvironment();
            startGame();
        }
        String s = ("Starting player " + startingPlayer + "with bet: " + higherBet);
        screen.diplayBettingWinner(s);
    }

    private void chooseFellow() {
        gameOrder(players,startingPlayer);
        ChoseFellowScreen screen1 = new ChoseFellowScreen(players.get(0),lock);
        screen1.setVisible(true);
        if(players.get(0) instanceof ControlledPlayer) {
            synchronized (lock) {
                while (!screen1.isFellowChosen()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            fellowCard = screen1.getCardChosen();

        }
        else if(players.get(0) instanceof AIPlayer) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AIPlayer player = (AIPlayer) players.get(0);
            fellowCard = player.chooseFellow();
            screen1.endPhase(fellowCard);
        }
        briscola = fellowCard.getSeme();
        System.out.println(fellowCard);

        screen.setListenerEnabled(true);

        screen.updatePlayerCards(players.get(0));
        screen.setLabelText(players.get(0));
        screen.update(screen.getGraphics());
        screen.revalidate();
        screen.repaint();

        createTeams();
        screen.setBetAreaVisibility(false);

    }

    private String makeScoreBoard() {
        int teamCallerScore= 0;
        int teamPopoloScore = 0;
        ArrayList<Player> scoreOrder = new ArrayList<>(players);
        scoreOrder.sort(Player::compareTo);
        int i = 1;
        StringBuilder s = new StringBuilder();
        for (Player p: scoreOrder) {
            if(teamCaller.contains(p)) {
                teamCallerScore+=p.getScore();
            }
            else {
                teamPopoloScore+=p.getScore();
            }
            s.append("\n").append(i).append(". ").append(p.getClass().getSimpleName()).append(": ").append(p.getOrder()).append(" Score: ").append(p.getScore());
            i++;
        }
        s.append("\nTeamCaller score: ").append(teamCallerScore).append("\nTeamPopolo score: ").append(teamPopoloScore);

        return s.toString();
    }

    private void playPhase() {

        int hands = 0;
        Card c = null;

        while(hands!=8) {
            System.out.println(briscola);
            Table table = new Table(briscola);
            Hand hand = new Hand();
            for (Player p:players) {
                screen.setTableVisibility(true);
                switchScreen(p);
                if(p instanceof ControlledPlayer) {
                    synchronized (lock) {
                        while (!screen.isTurnDone()) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    c = p.pickACard(screen.getImageString());
                }
                else if(p instanceof AIPlayer) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AIPlayer player = (AIPlayer) p;
                    c = player.throwCard();
                    player.getHand().chooseCard(c);
                }
                table.addCard(c, p);
                hand.addCard(c);
                screen.updateTableCards(c);
                System.out.println(p.getClass().getSimpleName() + p.getOrder());
                System.out.println(hand);
                screen.setTurnDone(false);
            }
            hands++;
            System.out.println(players.get(table.getWinner()).getClass().getSimpleName() + table.getWinner());
            players.get(table.getWinner()).winHand(table.getCards());
            screen.displayHandWinner(players.get(table.getWinner()));
            gameOrder(players,table.getWinner());

        }
        screen.diplayScoreBoard(makeScoreBoard());
        resetGameEnvironment();
        startGame();
    }

    private void goToMenuScreen() {
        NewGameScreen screen = new NewGameScreen(lock);
        screen.getFrame().setVisible(true);
        synchronized (lock) {
            while (!screen.isGameChosen()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.gameType = screen.getGameType();
            resetGameEnvironment();



        }
        this.screen.setVisible(true);
    }

    private void switchScreen(Player p) {
        screen.setCurrentPlayer(p);
        if (p.getOrder() != 0 || !isFirst) {
            screen.updatePlayerCards(p);
        }
        screen.setLabelText(p);
    }

    private void generatePlayers() {
        switch (gameType) {
            case CONTROLLED:
                for(int i = 0; i< 5; i++) {
                    ControlledPlayer player = new ControlledPlayer(i);
                    player.setCurrentBet(0);
                    players.add(player);
                }
                break;
            case PLAYERVSAI:
                ControlledPlayer player = new ControlledPlayer(0);
                player.setCurrentBet(0);
                players.add(player);
                for(int i = 1; i < 5; i++) {
                    AIPlayer player1 = new AIPlayerRandom(i);
                    player1.setCurrentBet(0);
                    players.add(player1);
                }
                break;
            case SIMULATED:
                for(int i = 0; i < 5; i++) {
                    AIPlayer player1 = new AIPlayerRandom(i);
                    player1.setCurrentBet(0);
                    players.add(player1);
                }

                break;
        }
    }

    private static <T> void gameOrder(ArrayList<T> players, int shift)
    {
        if (players.size() == 0)
            return;

        T element;
        for(int i = 0; i < shift; i++)
        {
            // remove first element, add it to the end of the ArrayList
            element = players.remove( 0);
            players.add(element);
        }

    }

    private void createTeams() {
        this.teamCaller = new ArrayList<>();
        this.teamPopolo = new ArrayList<>();

        for (Player p:players) {
            if(p.getOrder() == 0) {
                teamCaller.add(p);
            }
            else if(p.getHand().getCards().contains(fellowCard)) {
                teamCaller.add(p);
            }
            else {
                teamPopolo.add(p);
            }
        }
    }

    private void distributeCard() {
        for (int j = 0; j<8; j++) {
            for(int i = 0; i < 5; i++) {
                players.get(i).draw(deck.distributeCard());
            }
        }
    }

    @Override
    public String toString() {
        return "GameManagement{" +
                "players=" + players +
                '}';
    }
}
