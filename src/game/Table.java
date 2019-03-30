package game;

import card_management.Card;
import card_management.Semi;
import game.players.Player;

import java.util.ArrayList;

public class Table {
    private ArrayList<Card> cards;
    private boolean isFirst;
    private String winner;
    private int startingPlayer;
    private Card winningCard;
    private Semi briscola;
    private Hand hand;

    public Table(Semi briscola) {
        this.startingPlayer=0;
        this.cards = new ArrayList<>();
        this.briscola = briscola;
        isFirst = true;
        this.hand = new Hand(cards);
    }

    public void addCard(Card card, Player player) {
        if(isFirst) {
            tempWinner(card, player);
            isFirst = false;
        }
        else {
            if(winningCard.getSeme() == briscola && card.getSeme() == briscola && card.isGreaterStessoSeme(winningCard)) {
                tempWinner(card,player);
            }
            else if (card.getSeme() == winningCard.getSeme() && card.isGreaterStessoSeme(winningCard)) {
                tempWinner(card, player);
            }
            else {
                if(card.getSeme() == briscola && winningCard.getSeme()!=briscola) {
                tempWinner(card,player);
                }
            }
        }
        System.out.println(winningCard);
        card.setOwner(player.getRole());
        this.cards.add(card);
    }

    private void tempWinner(Card card, Player player) {
        winner = player.getPlayerID();
        winningCard = card;
        startingPlayer = player.getOrder();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Hand getHand() {
        return this.hand;
    }

    public String getWinner() {
        return winner;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }
}
