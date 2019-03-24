package game;

import card_management.Card;

import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Comparable<Player> {
    private int order;
    private Hand hand;
    private ArrayList<Card> wonCards;
    private int score;

    public Player(int order) {
        this.order = order;
        this.wonCards = new ArrayList<>();
        this.hand = new Hand();
        this.score = 0;
    }
    public  void draw(Card card) {
        this.hand.draw(card);
    }

    public Card chooseCard(int choose) {
        return this.hand.chooseCard(choose);
    }

    @Override
    public String toString() {
        return "Player{"  + order +
                "\nhand=" + hand +
                "}\nWon Cards:" + wonCards;
    }

    public int getOrder() {
        return order;
    }

    public Hand getHand() {
        return hand;
    }

    public Card pickACard() {
        int i = 0;
        System.out.println("Player " + order );
        System.out.println("Pick a card");
        for (Card c:hand.getCards()) {
            System.out.print("[" + i + "]" + c + " ");
            i++;
        }
        System.out.print("\n");
        Scanner scanner = new Scanner(System.in);
        int scelta = scanner.nextInt();
        if(scelta >= 0 && scelta < hand.getCards().size()) {
            return chooseCard(scelta);
        }
        else {
            return null;
        }
    }

    public void winHand(ArrayList<Card> cards) {
        this.wonCards.addAll(cards);
        for (Card c:cards) {
            this.score += c.getPoints();
        }
    }

    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.score, this.score);
    }

    public int getScore() {
        return score;
    }
}
