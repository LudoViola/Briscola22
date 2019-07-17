package game_management.players;

import card_management.Card;
import card_management.Hand;

import java.util.ArrayList;

public abstract class Player implements Comparable<Player> {
      private int order;
     protected Hand hand;
    ArrayList<Card> wonCards;
     int score;
     String playerID;
    private boolean flag = true;
     protected PlayerRole role;

     Player() {
    }

    public Player(int order) {
        this.order = order;
        this.wonCards = new ArrayList<>();
        this.hand = new Hand();
        this.score = 0;
        playerID = "Player " + order;
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

    public String getPlayerID() {
        return playerID;
    }

    public void setCurrentBet(int currentBet) {
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public PlayerRole getRole() {
        return role;
    }

    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.score, this.score);
    }

    public int getScore() {
        return score;
    }

    public void divideCardForSuit() {
        if(flag) {
            this.hand.divideCardsForSuit();
            flag = false;
        }
    }

    @Override
    public String toString() {
        return playerID;
    }
}
