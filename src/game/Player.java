package game;

import card_management.Card;

public class Player {
    private int order;
    private Hand hand;

    public Player(int order) {
        this.order = order;
        this.hand = new Hand();
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
                ", hand=" + hand +
                '}';
    }

    public int getOrder() {
        return order;
    }
}
