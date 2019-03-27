package game;

import card_management.Card;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.awt.*;
import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    Hand() {
        this.cards = new ArrayList<>();
    }

    void draw(Card card) {
        this.cards.add(card);
    }

    public Hand(ArrayList<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    Card chooseCard(String string) {
        Card card = null;
        for (Card c:cards) {
            if(c.getCardImage().toString().equals(string)) {
                card = c;
            }
        }
        this.cards.remove(card);
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

    public void addCard(Card cards) {
        if(!this.cards.contains(cards)) {
            this.cards.add(cards);
        }
    }
}
