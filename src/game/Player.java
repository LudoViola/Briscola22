package game;

import card_management.Card;

import java.awt.*;
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

    public Card chooseCard(String image) {
        return this.hand.chooseCard(image);
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

    public Card pickACard(String string) {
            return chooseCard(string);

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
