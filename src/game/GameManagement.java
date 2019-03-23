package game;

import card_management.Deck;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class GameManagement {
    private ArrayList<Player> players;
    private Deck deck;

    public GameManagement(Deck deck) {
        this.players = new ArrayList<>();
        this.deck = deck;
        generatePlayers();
    }

    public void distributeCard() {
        for (int j = 0; j<8; j++) {
        for(int i = 0; i < 5; i++) {
                players.get(i).draw(deck.distributeCard());
            }
        }
    }

    public void startGame() {
        bettingTurn();
    }

    private void bettingTurn() {

        int higherBet = 0;
        int startingPlayer = 0;
        CopyOnWriteArrayList<Player> bettingPlayers = new CopyOnWriteArrayList<>(players);
        while (bettingPlayers.size()!=1) {
            for (Player p : bettingPlayers) {
                    System.out.println("Player number: " + p.getOrder() + "\nChose points bet from 61 to 120,0 to pass");
                    System.out.println("Bet to beat: " + higherBet);
                    Scanner scanner = new Scanner(System.in);
                    int bet = scanner.nextInt();
                    if (bet > 60 && bet < 121) {
                        if (bet > higherBet) {
                            higherBet = bet;
                            startingPlayer = p.getOrder();
                        } else {
                            System.out.println("Bet to beat: " + higherBet);
                            System.out.println("Chose again or pass");
                        }
                    } else if (bet == 0) {
                        bettingPlayers.remove(p);
                    } else {
                        System.out.println("Chose again or pass");
                    }
            }
        }
        System.out.println("Starting player " + startingPlayer + "with bet: " + higherBet);
        players = rotate(players,(startingPlayer));
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
            // remove last element, add it to front of the ArrayList
            element = aL.remove( 0);
            aL.add(aL.size()-1, element);
        }

        return aL;
    }
}
