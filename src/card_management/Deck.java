package card_management;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;
    private final static int NUMBER_OF_CARDS = 40;

    public Deck() {
        deck = new ArrayList<>();
        createDeck();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    private  void createDeck() {
        for(Semi s: Semi.values()) {
            for(int i = 1; i < 11; i++) {
                Card card = new Card(s,i);
                deck.add(card);
            }
        }
    }

    public Card distributeCard() {
        if(deck.size()>0) {
            Card card = this.deck.get(deck.size() - 1);
            this.deck.remove(deck.size() - 1);
            return card;
        }
        else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck.toString() +
                '}';
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

}
