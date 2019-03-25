package game;

import card_management.Card;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    Hand() {
        this.cards = new ArrayList<>();
    }

    void draw(Card card) {
        this.cards.add(card);
    }

    Card chooseCard(int choose) {
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

    public ArrayList<Card> getCards() {
        return cards;
    }
}
