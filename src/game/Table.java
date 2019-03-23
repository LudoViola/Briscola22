package game;

import card_management.Card;
import card_management.Semi;

import java.util.ArrayList;

public class Table {
    private ArrayList<Card> cards;
    private ArrayList<Card> briscolas;
    private boolean isFirst = true;
    private Semi semeDiTurno;
    private int winner;
    private Card winningCard;
    private Semi briscola;

    public Table(Semi briscola) {
        this.cards = new ArrayList<>();
        this.briscola = briscola;
    }

    public void addCard(Card card, Player player) {
        if(isFirst) {
            semeDiTurno = card.getSeme();
            tempWinner(card, player);
            isFirst = false;
        }
        else {
            if(semeDiTurno == briscola && card.isGreaterStessoSeme(winningCard)) {
                tempWinner(card,player);
            }
            else {
                if (card.getSeme() == semeDiTurno && card.isGreaterStessoSeme(winningCard)) {
                    tempWinner(card, player);
                }
                else if(card.getSeme() == briscola && winningCard.getSeme()!=briscola) {
                    semeDiTurno = briscola;
                    tempWinner(card,player);
                }
                else {
                    this.cards.add(card);
                }
            }
        }
    }

    private void tempWinner(Card card, Player player) {
        winner = player.getOrder();
        winningCard = card;
        this.cards.add(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getWinner() {
        return winner;
    }
}
