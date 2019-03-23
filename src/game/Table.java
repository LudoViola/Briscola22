package game;

import card_management.Card;
import card_management.Semi;

import java.util.ArrayList;

public class Table {
    private ArrayList<Card> cards;
    private boolean isFirst = true;
    private Semi semeDiTurno;
    private int winner;
    private Card winningCard;

    public Table() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card, Player player) {
        if(isFirst) {
            semeDiTurno = card.getSeme();
            tempWinner(card, player);
        }
        else {
            if(card.getSeme() == semeDiTurno && card.isGreaterStessoSeme(winningCard)) {
                tempWinner(card, player);
            }
        }
    }

    private void tempWinner(Card card, Player player) {
        winner = player.getOrder();
        winningCard = card;
        this.cards.add(card);
    }

    public void checkWinner() {

    }
}
