package game_management.players;

import card_management.Card;
import card_management.Hand;

import java.util.ArrayList;

public class OnlinePlayer {
    private String playerName;
    Hand hand;
    ArrayList<Card> wonCards;
    int score;
    int currentBet;
    private boolean flag = true;

    public OnlinePlayer(String playerName) {
        this.playerName = playerName;
        this.wonCards = new ArrayList<>();
        this.hand = new Hand();
        this.score = 0;
    }

    public  void draw(Card card) {
        this.hand.draw(card);
    }
    public void sortHand() {
        this.hand.getCards().sort(Card::compareTo);
    }

    @Override
    public String toString() {
        return "OnlinePlayer{" +
                "playerName='" + playerName + '\'' +
                ", hand=" + hand +
                '}';
    }
}
