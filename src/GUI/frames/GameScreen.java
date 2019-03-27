package GUI.frames;

import GUI.buttons.ButtonCardImage;
import GUI.panels.CardsGroupPanel;
import GUI.panels.TableCardPanel;
import card_management.Card;
import finals.MyColors;
import game.Hand;
import game.Player;
import game.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameScreen extends JFrame implements ActionListener {

    private JPanel backgroundPanel;
    private JPanel playerCardZone;
    private JPanel tablePanel;
    private JPanel innerTablePanel;
    private CardsGroupPanel cardsContainer;
    private TableCardPanel tableCards;
    private Player currentPlayer;
    private JLabel playerName;
    private JButton buttonBet;
    private boolean betDone;
    private boolean turnDone;
    private boolean listenerEnabled;
    private int bet;
    private JButton buttonPass;
    private JSpinner betSpinner;
    private final Object lock;
    private String imageString;
    private Table table;
    private int higherBet;


    public GameScreen(Player firstPlayer, Object lock) throws HeadlessException {



        setTitle("Briscola in 5 Partita Simulata");
        setSize(1000,800);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        backgroundPanel = new JPanel();
        backgroundPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        backgroundPanel.setPreferredSize(getSize());
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(MyColors.brown);
        rightPanel.setPreferredSize(new Dimension(100,1000));

        SpinnerNumberModel model = new SpinnerNumberModel(61,61,120,1);
        betSpinner = new JSpinner(model);
        betSpinner.setBackground(MyColors.brown);
        betSpinner.setForeground(Color.BLACK);

        JPanel betPanel = new JPanel();
        betPanel.setLayout(new GridLayout(4,1));
        betPanel.setBackground(MyColors.brown);
        betPanel.setBorder(BorderFactory.createEmptyBorder(160,0,0,0));

        playerName = new JLabel("Player0");
        playerName.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        playerName.setBackground(MyColors.brown);
        buttonBet = new JButton("BET");
        buttonBet.setBackground(MyColors.brown);
        buttonBet.addActionListener(this);
        buttonBet.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        buttonPass = new JButton("PASS");
        buttonPass.setActionCommand("PASS");
        buttonPass.setBackground(MyColors.brown);
        buttonPass.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        buttonPass.addActionListener(this);

        rightPanel.add(betPanel);
        betPanel.add(playerName);
        betPanel.add(buttonBet);
        betPanel.add(betSpinner);
        betPanel.add(buttonPass);

        tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(200,100));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        tablePanel.setBackground(Color.GREEN);

        innerTablePanel = new JPanel();

        tableCards = new TableCardPanel();
        innerTablePanel.add(tableCards.getPanel());
        innerTablePanel.setVisible(false);

        tablePanel.add(innerTablePanel);

        playerCardZone = new JPanel();
        playerCardZone.setPreferredSize(new Dimension(200,200));
        playerCardZone.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        playerCardZone.setLayout(new GridLayout(1,3));
        playerCardZone.setBackground(Color.WHITE);

        JPanel menuZone = new JPanel();
        menuZone.setPreferredSize(new Dimension(200,50));
        menuZone.setBackground(Color.BLACK);

        cardsContainer = new CardsGroupPanel(firstPlayer.getHand());
        playerCardZone.add(cardsContainer.getPanel());

        backgroundPanel.add(menuZone, BorderLayout.PAGE_START);
        backgroundPanel.add(tablePanel,BorderLayout.CENTER);
        backgroundPanel.add(rightPanel,BorderLayout.LINE_START);
        backgroundPanel.add(playerCardZone, BorderLayout.PAGE_END);


        add(backgroundPanel);

        betDone = false;
        turnDone = false;
        listenerEnabled = false;
        bet = 0;
        this.lock = lock;

    }

    public void diplayBettingWinner(String s) {
        JOptionPane.showMessageDialog(this,s,"The Winner", JOptionPane.INFORMATION_MESSAGE);
    }
    public void diplayScoreBoard( String s) {
        JOptionPane.showMessageDialog(this,s,"Classifica", JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayHandWinner(Player p) {
        String s = "Player " + p.getOrder();
        JOptionPane.showMessageDialog(this,s,"Hand Winner", JOptionPane.INFORMATION_MESSAGE);
        tableCards.update(p);
    }


    public void updateTableCards(Card card) {
            tableCards.update(card);
            revalidate();
            repaint();
    }


    public void updatePlayerCards(Player player) {
        cardsContainer.update(player.getHand());
        revalidate();
        repaint();
        if(listenerEnabled) {
            setActionListener();
        }
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isBetDone() {
        return betDone;
    }

    public boolean isTurnDone() {
        return turnDone;
    }

    public void setTurnDone(boolean turnDone) {
        this.turnDone = turnDone;
    }

    public void setBetDone(boolean betDone) {
        this.betDone = betDone;
    }

    public void setListenerEnabled(boolean listenerEnabled) {
        this.listenerEnabled = listenerEnabled;
    }

    public int getBet() {
        return bet;
    }

    public void setHigherBet(int higherBet) {
        this.higherBet = higherBet;
    }

    public String getImageString() {
        return imageString;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonBet) {
            bet = (Integer) betSpinner.getValue();
            if(bet > higherBet) {
                synchronized (lock) {
                    betDone = true;
                    lock.notifyAll();
                }
            }
        }
        else if(e.getSource() == buttonPass) {
            bet = 0;
            synchronized (lock) {
                betDone = true;
                lock.notifyAll();
            }
        }
        else if(e.getSource() instanceof ButtonCardImage) {
            imageString = e.getActionCommand();
            synchronized (lock) {
                turnDone = true;
                lock.notifyAll();
            }
        }
    }

    public void setLabelText(int order) {
        playerName.setText("Player" + order);
    }

    public void setBetAreaVisibility(boolean b) {
        buttonBet.setVisible(b);
        buttonPass.setVisible(b);
        betSpinner.setVisible(b);
    }

    public void setTableVisibility(boolean b) {
        innerTablePanel.setVisible(b);
    }

    public void setActionListener() {
        cardsContainer.setActionListener(this);
    }
}
