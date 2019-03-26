package game;

import GUI.frames.ChoseFellowScreen;
import GUI.frames.BettingTurnScreen;
import card_management.Card;
import card_management.Deck;
import card_management.Semi;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class GameManagement {
    private ArrayList<Player> players;
    private Deck deck;
    private Scanner scanner;
    private Semi briscola;
    private Card fellowCard;
    private int startingPlayer;
    private int currentPlayer;
    private int higherBet;
    private int currentBet;
    private CopyOnWriteArrayList<Player> bettingPlayers;
    private boolean betAnswer;
    private BettingTurnScreen screen;
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
        currentPlayer = 0;
        isFirst = true;


        this.lock = new Object();

        this.screen = new BettingTurnScreen(players.get(0), lock);
        this.screen.setVisible(true);

    }

    public void distributeCard() {
        for (int j = 0; j<8; j++) {
        for(int i = 0; i < 5; i++) {
                players.get(i).draw(deck.distributeCard());
            }
        }
    }

    public void startGame() {
        betAnswer = false;
        bettingTurn();
        chooseFellow();
        playPhase();
        makeScoreBoard();
    }



    private void makeScoreBoard() {
        ArrayList<Player> scoreOrder = new ArrayList<>(players);
        scoreOrder.sort(Player::compareTo);
        int i = 1;
        for (Player p: scoreOrder) {
            System.out.println(i +". Player: " + p.getOrder()+" Score: " + p.getScore());
            i++;
        }
    }

    private void playPhase() {
        rotate(players,(startingPlayer));
        int hands = 0;
        while (hands!=8) {
            Table table = new Table(briscola);
            for(int i = 0; i < 5;i++) {
                table.addCard(players.get(i).pickACard(), players.get(i));
            }

            players.get(table.getWinner()).winHand(table.getCards());
            makeScoreBoard();
            hands++;
        }
    }

    private void chooseFellow() {
        rotate(players,startingPlayer);
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

        screen.updatePlayerCards(players.get(0));
        screen.setLabelText(players.get(0).getOrder());
        screen.update(screen.getGraphics());
        screen.revalidate();
        screen.repaint();

    }

    public void bettingTurn() {
        while (bettingPlayers.size()!=1) {
            for (Player p : bettingPlayers) {
                screen.setCurrentPlayer(p);
                if(p.getOrder()==0 && isFirst) {

                }
                else {
                    screen.updatePlayerCards(p);
                }
                screen.setLabelText(p.getOrder());

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

    public static <T> ArrayList<T> rotate(ArrayList<T> aL, int shift)
    {
        if (aL.size() == 0)
            return aL;

        T element = null;
        for(int i = 0; i < shift; i++)
        {
            // remove first element, add it to the end of the ArrayList
            element = aL.remove( 0);
            aL.add(element);
        }

        return aL;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public Semi getBriscola() {
        return briscola;
    }

    public Card getFellowCard() {
        return fellowCard;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public int getHigherBet() {
        return higherBet;
    }
}
