package game_management.players.ai;

import card_management.Card;
import card_management.Deck;
import card_management.Semi;
import game_management.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public abstract class AIPlayer extends Player implements AI {

    protected Card tempWinningCard;
    protected Player tempWinningPlayer;
    Semi briscola;
    int[] cardsForSuit;
    HashMap<Semi,Integer> cardsBySuit;


    AIPlayer(int order) {
        super(order);
        tempWinningCard = null;
    }

    public void setCardsForSuit() {
        this.cardsForSuit = this.hand.getCardsForSuit();
        this.cardsBySuit = new HashMap<>();
        for (Semi s: Semi.values()) {
            cardsBySuit.put(s,cardsForSuit[s.ordinal()]);
        }
    }


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
     Card giveLiscio() {
        Card card = null;
        for (Card c:this.hand.getCards()) {
            if(c.getPoints()==0 && c.getSeme()!=briscola) {
                card = c;
            }
        }
        if(card == null) {
            card = giveRandomCard();
        }
        return card;
    }

     Card giveCarico() {
        Card card = null;
        for (Card c:this.hand.getCards()) {
            if((c.getPoints()==10 || c.getPoints()==11) && c.getSeme()!=briscola) {
                card = c;
            }
        }
        if(card == null) {
            card = giveRandomCard();
        }
        return card;
    }

     Card getCardToWin() {
        Card d = null;
        if(tempWinningCard!=null) {
            for (Card c : this.hand.getCards()) {
                if (c.getSeme() == briscola && tempWinningCard.getSeme() == briscola && c.isGreaterStessoSeme(tempWinningCard)) {
                    d = c;
                    break;
                } else if (c.getSeme() == tempWinningCard.getSeme() && c.isGreaterStessoSeme(tempWinningCard)) {
                    d = c;
                    break;
                } else {
                    if (c.getSeme() == briscola && tempWinningCard.getSeme() != briscola) {
                        d = c;
                        break;
                    }
                }
            }

            if(d == null) {
                d = giveLiscio();
            }

            return d;
        }
        else {
            return giveLiscio();
        }
    }

    private Card giveRandomCard() {
        Random random = new Random();
        return this.hand.getCards().get(random.nextInt(this.hand.getCards().size()));
    }

    public void setTempWinningCard(Card tempWinningCard) {
        this.tempWinningCard = tempWinningCard;
    }

    public void setTempWinningPlayer(Player tempWinningPlayer) {
        this.tempWinningPlayer = tempWinningPlayer;
    }

}

