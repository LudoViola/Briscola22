package GUI.panels;

import card_management.Card;
import card_management.Hand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CardsGroupPanel {
    private ArrayList<CardPanel> cardsSlot;
    private JPanel panel;


    public CardsGroupPanel(Hand hand) {
        panel = new JPanel();
        cardsSlot = new ArrayList<>();
        panel.setLayout(new GridLayout(1,8));
        for (Card c:hand.getCards()) {
            addSlot(c);

        }
    }

    private void addSlot(Card c) {
        CardPanel cardSlot = new CardPanel(c.getCardImage());
        cardSlot.setBackground(new Color(95, 54, 0));
        cardSlot.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        panel.add(cardSlot);
        cardsSlot.add(cardSlot);
    }

    public void update(Hand hand,boolean isControlled) {
        if(isControlled) {
            for (CardPanel p : cardsSlot) {
                panel.remove(p);
            }
            cardsSlot.clear();
            for (Card c : hand.getCards()) {
                addSlot(c);
            }
            panel.revalidate();
            panel.repaint();
        }
    }

    public void setBackground(Color color) {
        this.panel.setBackground(color);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setActionListener(ActionListener listener) {
        for (CardPanel p:cardsSlot) {
            p.setActionListener(listener);
        }
    }
}
