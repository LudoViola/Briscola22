package GUI.panels;

import card_management.Card;
import game.Hand;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelTurnPlayerCards {
    private ArrayList<PanelTurningCard> cardsSlot;
    private JPanel panel;

    public PanelTurnPlayerCards(Hand hand) {
        panel = new JPanel();
        cardsSlot = new ArrayList<>();
        panel.setLayout(new GridLayout(1,8));
        for (Card c:hand.getCards()) {
            PanelTurningCard cardSlot = new PanelTurningCard(c.getCardImage());
            cardSlot.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
            panel.add(cardSlot);
            cardsSlot.add(cardSlot);

        }
    }

    public void update(Hand hand) {
        for (PanelTurningCard p:cardsSlot) {
            panel.remove(p);
        }
        cardsSlot.clear();
        for (Card c:hand.getCards()) {
            PanelTurningCard cardSlot = new PanelTurningCard(c.getCardImage());
            cardSlot.setBorder(BorderFactory.createEmptyBorder(0,1,0,1));
            panel.add(cardSlot);
            cardsSlot.add(cardSlot);
        }
        panel.revalidate();
        panel.repaint();
    }

    public JPanel getPanel() {
        return panel;
    }
}
