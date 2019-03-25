package GUI;

import card_management.Card;
import game.Hand;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelTurnPlayerCards extends JPanel {
    private ArrayList<PanelTurningCard> cardsSlot;

    public PanelTurnPlayerCards(Hand hand) {
        cardsSlot = new ArrayList<>();
        setLayout(new GridLayout(1,8));
        for (Card c:hand.getCards()) {
            PanelTurningCard cardSlot = new PanelTurningCard(c.getCardImage());
            cardSlot.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
            add(cardSlot);
            cardsSlot.add(cardSlot);
        }
    }

    public void update(Hand hand) {
        for (PanelTurningCard p:cardsSlot) {
            remove(p);
        }
        cardsSlot.clear();
        for (Card c:hand.getCards()) {
            PanelTurningCard cardSlot = new PanelTurningCard(c.getCardImage());
            cardSlot.setBorder(BorderFactory.createEmptyBorder(0,1,0,1));
            add(cardSlot);
            cardsSlot.add(cardSlot);
        }
    }
}
