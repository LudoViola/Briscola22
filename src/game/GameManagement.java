package game;

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
        chooseFellow();
        playPhase();
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
            hands++;
        }
    }

    private void chooseFellow() {
        boolean check = true;
        while(check) {
            System.out.println("Player"+ startingPlayer + " : Choose your Fellow");
            System.out.println("[0]COPPE\n[1]DENARI\n[2]BASTONI\n[3]SPADE\n");
            scanner = new Scanner(System.in);
            int semeScelto = scanner.nextInt();
            if (semeScelto < 4 && semeScelto >= 0) {
                for (Semi s : Semi.values()) {
                    if (s.ordinal() == semeScelto) {
                        briscola = s;
                        check = false;
                    }
                }
            } else {
                System.out.println("Invalid choice");
            }
        }
        while (!check) {
            System.out.println("Choose now card value, from 1 to 10");
            scanner = new Scanner(System.in);
            int valoreScelto = scanner.nextInt();
            if(valoreScelto>0 && valoreScelto<11) {
                fellowCard = new Card(briscola,valoreScelto);
                boolean check1 = true;
                for (Card c:players.get(startingPlayer).getHand().getCards()) {
                    if(fellowCard.equals(c)) {
                        check1 = false;
                    }
                }

                if (check1) {
                    check = true;
                }
                else {
                    System.out.println("Invalid choice, choose again");
                    fellowCard = null;
                }
            }
            else {
                System.out.println("Invalid choice, choose again");
            }
        }

        System.out.println("FellowCard " + fellowCard + "briscola: " + briscola);
    }

    private void bettingTurn() {

        int higherBet = 0;
        startingPlayer = 0;
        CopyOnWriteArrayList<Player> bettingPlayers = new CopyOnWriteArrayList<>(players);
        while (bettingPlayers.size()!=1) {
            for (Player p : bettingPlayers) {
                    System.out.println("Player number: " + p.getOrder() + "\nChose points bet from 61 to 120,0 to pass");
                    System.out.println("Bet to beat: " + higherBet);
                    scanner = new Scanner(System.in);
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
}
