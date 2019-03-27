package game;

import GUI.frames.ChoseFellowScreen;
import GUI.frames.GameScreen;
import card_management.Card;
import card_management.Deck;
import card_management.Semi;

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
    private Deck deckCopy;

    public GameManagement() {
        this.players = new ArrayList<>();
        this.deck = new Deck();
        this.deck.shuffle();
        this.deckCopy =deck;
        generatePlayers();
        distributeCard();
        bettingPlayers = new CopyOnWriteArrayList<>(players);
        higherBet = 0;
        startingPlayer = 0;
        isFirst = true;


        this.lock = new Object();

        this.screen = new GameScreen(players.get(0), lock);
        this.screen.setVisible(false);

    }

    public void distributeCard() {
        for (int j = 0; j<8; j++) {
        for(int i = 0; i < 5; i++) {
                players.get(i).draw(deck.distributeCard());
            }
        }
    }

    public void startGame() {
        this.screen.setVisible(true);
        betAnswer = false;
        bettingTurn();
        chooseFellow();
        playPhase();
        makeScoreBoard();
    }



    private String makeScoreBoard() {
        int teamCallerScore= 0;
        int teamPopoloScore = 0;
        ArrayList<Player> scoreOrder = new ArrayList<>(players);
        scoreOrder.sort(Player::compareTo);
        int i = 1;
        String s = "";
        for (Player p: scoreOrder) {
            if(teamCaller.contains(p)) {
                teamCallerScore+=p.getScore();
            }
            else {
                teamPopoloScore+=p.getScore();
            }
            s+=("\n" + i + ". Player: " + p.getOrder() + " Score: " + p.getScore());
            i++;
        }
        s+= ("\nTeamCaller score: " +teamCallerScore+"\nTeamPopolo score: "+ teamPopoloScore);

        return s;
    }

    private void playPhase() {

        int hands = 0;
        Card c = null;

        while(hands!=8) {
            Table table = new Table(briscola);
            Hand hand = new Hand();
            for (Player p:players) {
                    screen.setTableVisibility(true);

                switchScreen(p);
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
                table.addCard(c, p);
                hand.addCard(c);
                screen.updateTableCards(c);
                System.out.println(hand);
                screen.setTurnDone(false);
            }
            hands++;
            players.get(table.getWinner()).winHand(table.getCards());
            screen.displayHandWinner(players.get(table.getWinner()));
            gameOrder(players,table.getWinner());

        }
        screen.diplayScoreBoard(makeScoreBoard());
        screen.dispose();
    }

    private void switchScreen(Player p) {
        screen.setCurrentPlayer(p);
        if (p.getOrder() != 0 || !isFirst) {
            screen.updatePlayerCards(p);
        }
        screen.setLabelText(p.getOrder());
    }

    private void chooseFellow() {
        gameOrder(players,startingPlayer);
        ChoseFellowScreen screen1 = new ChoseFellowScreen(players.get(0),lock);
        screen1.setVisible(true);

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
        briscola = fellowCard.getSeme();
        System.out.println(fellowCard);

        screen.setListenerEnabled(true);

        screen.updatePlayerCards(players.get(0));
        screen.setLabelText(players.get(0).getOrder());
        screen.update(screen.getGraphics());
        screen.revalidate();
        screen.repaint();

        createTeams();
        screen.setBetAreaVisibility(false);

    }

    public void bettingTurn() {
        while (bettingPlayers.size()!=1) {
            for (Player p : bettingPlayers) {
                switchScreen(p);

                synchronized (lock) {
                    while (!screen.isBetDone()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                int bet = screen.getBet();
                if (bet > 60 && bet < 121) {
                    if (bet > higherBet) {
                        higherBet = bet;
                        startingPlayer = p.getOrder();
                    }
                }
                 else if (bet == 0) {
                    bettingPlayers.remove(p);
                }
                screen.setBetDone(false);
                 if(bettingPlayers.size()== 0) {
                     bettingPlayers.add(new Player(6));
                 }
                 isFirst = false;
            }
        }
            String s = ("Starting player " + startingPlayer + "with bet: " + higherBet);
            screen.diplayBettingWinner(s);
    }

    private void generatePlayers() {

        for(int i = 0; i< 5; i++) {
            Player player = new Player(i);
            players.add(player);
        }
    }

    @Override
    public String toString() {
        return "GameManagement{" +
                "players=" + players +
                '}';
    }

    public static <T> ArrayList<T> gameOrder(ArrayList<T> players, int shift)
    {
        if (players.size() == 0)
            return players;

        T element = null;
        for(int i = 0; i < shift; i++)
        {
            // remove first element, add it to the end of the ArrayList
            element = players.remove( 0);
            players.add(element);
        }

        return players;
    }

    public Deck getDeck() {
        return deck;
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

}
