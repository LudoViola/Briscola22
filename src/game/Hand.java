package game;

import card_management.Card;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void draw(Card card) {
        this.cards.add(card);
    }

    public Card chooseCard(int choose) {
        Card card = this.cards.get(choose);
        this.cards.remove(choose);
        return card;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                '}';
    }
}
