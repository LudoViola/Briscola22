package game.players;

import card_management.Card;
import card_management.Deck;
import card_management.Semi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class AIPlayerEasy extends AIPlayer {
    private Random random = new Random();
    private Semi briscola;
    private int[] cardsForSuit;
    private HashMap<Semi,Integer> cardsBySuit;
    public AIPlayerEasy(int order) {
        super(order);
    }

    public void setCardsForSuit() {
        this.cardsForSuit = this.hand.getCardsForSuit();
        this.cardsBySuit = new HashMap<>();
        for (Semi s:Semi.values()) {
            cardsBySuit.put(s,cardsForSuit[s.ordinal()]);
        }
    }

    @Override
    public int chooseBet() {
       if(Collections.max(cardsBySuit.values()) >= 4) {
           return 81;
       }
       else if(Collections.max(cardsBySuit.values()) == 3) {
           return 71;
       }
       else if(Collections.max(cardsBySuit.values()) == 2) {
           return 61;
       }
       else {
           return 0;
       }
    }

    @Override
    public Card chooseFellow() {
        Deck deck = new Deck();
        deck.getDeck().removeAll(this.hand.getCards());
        int j = 0;
        for (Integer i:cardsForSuit) {
            if(i.equals(Collections.max(cardsBySuit.values()))) {
                briscola = Semi.values()[j];
            }
            j++;
        }
        ArrayList<Card> cards = new ArrayList<>();
        for (Card c:deck.getDeck() ) {
            if(c.getSeme().equals(briscola)) {
                cards.add(c);
            }
        }

        cards.sort(Card::compareTo);
        return  cards.get(cards.size()-1);
    }

    @Override
    public Card throwCard() {
        return this.hand.getCards().get(random.nextInt(this.hand.getCards().size()));
    }
}
