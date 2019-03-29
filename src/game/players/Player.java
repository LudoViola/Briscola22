package game.players;

import card_management.Card;
import game.Hand;

import java.util.ArrayList;

public abstract class Player implements Comparable<Player> {
     int order;
     Hand hand;
     ArrayList<Card> wonCards;
     int score;
     int currentBet;

    public Player(int order) {
        this.order = order;
        this.wonCards = new ArrayList<>();
        this.hand = new Hand();
        this.score = 0;
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

    public Card chooseCard(String image) {
        return this.hand.chooseCard(image);
    }

    public  void draw(Card card) {
        this.hand.draw(card);
    }


    public void sortHand() {
        this.hand.getCards().sort(Card::compareTo);
    }


    public int getOrder() {
        return order;
    }

    public Hand getHand() {
        return hand;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }



    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.score, this.score);
    }

    public int getScore() {
        return score;
    }

}
