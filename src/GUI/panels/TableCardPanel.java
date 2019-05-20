package GUI.panels;

import GUI.buttons.ButtonCardImage;
import card_management.Card;
import game_management.players.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TableCardPanel {

    private JPanel panel;
    private ArrayList<ButtonCardImage> cardsSlot;

    public TableCardPanel() {
        this.panel = new JPanel();
        cardsSlot = new ArrayList<>();
        panel.setLayout(new GridLayout(2,3));
        panel.setBackground(Color.GREEN);
    }

    public void update() {
        if(cardsSlot.size()== 5) {
            for (ButtonCardImage p : cardsSlot) {
                panel.remove(p);
            }
            cardsSlot.clear();
        }
    }

    public void update(Card card) {
        if(cardsSlot.size()== 5) {
            clear();
            addSlot(card);
        }
        else {
            addSlot(card);
        }
    }

    private void clear() {
        for (ButtonCardImage p : cardsSlot) {
            panel.remove(p);
        }
        cardsSlot.clear();
    }

    private void addSlot(Card card) {
        ButtonCardImage cardSlot = new ButtonCardImage(card.getCardImage());
        cardSlot.setBackground(Color.GREEN);
        cardSlot.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(cardSlot);
        cardsSlot.add(cardSlot);
    }

    public JPanel getPanel() {
        return panel;
    }
}
