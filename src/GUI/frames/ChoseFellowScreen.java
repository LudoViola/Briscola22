package GUI.frames;

import GUI.panels.CardPanel;
import card_management.Card;
import card_management.Deck;
import game.players.ControlledPlayer;
import game.players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChoseFellowScreen implements ActionListener {
    JFrame frame;
    Deck deckCopy;
    private JPanel background;
    private Card cardChosen;
    private boolean FellowChosen;
    private final Object lock;

    public ChoseFellowScreen(Player player, Object lock) {
        frame = new JFrame("Scegli Briscola");
        frame.setSize(1000,800);
        frame.setResizable(false);

        deckCopy = new Deck();
        deckCopy.getDeck().removeAll(player.getHand().getCards());

        background = new JPanel();
        background.setLayout(new GridLayout(4,8));
        fillCards();

        frame.add(background);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        FellowChosen = false;
        this.lock = lock;
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    public void fillCards() {
        for (Card c:deckCopy.getDeck()) {
            CardPanel cardSlot = new CardPanel(c.getCardImage());
            cardSlot.setActionListener(this);
            cardSlot.setBorder(BorderFactory.createEmptyBorder(0,1,0,1));
            background.add(cardSlot);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        findCard(e);
        synchronized (lock) {
            FellowChosen = true;
            lock.notifyAll();
        }
    }

    private void findCard(ActionEvent e) {
        for (Card c:deckCopy.getDeck()) {
            if(c.getCardImage().toString().equals(e.getActionCommand())) {
                cardChosen = c;
                endPhase(c);
            }
        }
    }

    public void endPhase(Card c) {
        //JOptionPane.showMessageDialog(frame.getContentPane(),c,"Card Chosen", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    public Card getCardChosen() {
        return cardChosen;
    }

    public boolean isFellowChosen() {
        return FellowChosen;
    }
}
