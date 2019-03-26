package GUI.frames;

import GUI.panels.PanelTurnPlayerCards;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BettingTurnScreen extends JFrame implements ActionListener {

    private JPanel backgroundPanel;
    private JPanel playerCardZone;
    private PanelTurnPlayerCards cardsContainer;
    private Player currentPlayer;
    private JLabel playerName;
    private JButton buttonBet;
    private boolean betDone;
    private int bet;
    private JButton buttonPass;
    private JSpinner betSpinner;
    private final Object lock;

    public BettingTurnScreen(Player firstPlayer, Object lock) throws HeadlessException {

        setTitle("Briscola in 5");
        setSize(1000,800);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        backgroundPanel = new JPanel();
        backgroundPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        backgroundPanel.setPreferredSize(getSize());
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(100,1000));

        SpinnerNumberModel model = new SpinnerNumberModel(61,61,120,1);
        betSpinner = new JSpinner(model);

        JPanel betPanel = new JPanel();
        betPanel.setLayout(new GridLayout(4,1));
        betPanel.setBorder(BorderFactory.createEmptyBorder(160,0,0,0));

        playerName = new JLabel("Player0");
        buttonBet = new JButton("BET");
        buttonBet.addActionListener(this);
        buttonPass = new JButton("PASS");
        buttonPass.setActionCommand("PASS");
        buttonPass.addActionListener(this);

        rightPanel.add(betPanel);
        betPanel.add(playerName);
        betPanel.add(buttonBet);
        betPanel.add(betSpinner);
        betPanel.add(buttonPass);

        JPanel table = new JPanel();
        table.setPreferredSize(new Dimension(200,100));
        table.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        table.setBackground(Color.GREEN);

        playerCardZone = new JPanel();
        playerCardZone.setPreferredSize(new Dimension(200,200));
        playerCardZone.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        playerCardZone.setLayout(new GridLayout(1,3));
        playerCardZone.setBackground(Color.WHITE);

        JPanel menuZone = new JPanel();
        menuZone.setPreferredSize(new Dimension(200,50));
        menuZone.setBackground(Color.BLACK);

        cardsContainer = new PanelTurnPlayerCards(firstPlayer.getHand());
        playerCardZone.add(cardsContainer.getPanel());

        backgroundPanel.add(menuZone, BorderLayout.PAGE_START);
        backgroundPanel.add(table,BorderLayout.CENTER);
        backgroundPanel.add(rightPanel,BorderLayout.LINE_START);
        backgroundPanel.add(playerCardZone, BorderLayout.PAGE_END);


        add(backgroundPanel);

        betDone = false;
        bet = 0;
        this.lock = lock;

/*


        JPanel table = new JPanel();
        table.setSize(900,400);
        table.setBackground(Color.GREEN);

        JPanel zone2 = new JPanel();
        zone2.setSize(900,300);
        zone2.setBackground(Color.BLACK);

        add(zone1);
        add(table);
        add(zone2);
        */
    }

    public void diplayBettingWinner(String s) {
        JOptionPane.showMessageDialog(this,s,"The Winner", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updatePlayerCards(Player player) {
        cardsContainer.update(player.getHand());
        revalidate();
        repaint();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isBetDone() {
        return betDone;
    }

    public void setBetDone(boolean betDone) {
        this.betDone = betDone;
    }

    public int getBet() {
        return bet;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonBet) {
            bet = (Integer) betSpinner.getValue();
            synchronized (lock) {
                betDone = true;
                lock.notifyAll();
            }
        }
        else if(e.getSource() == buttonPass) {
            bet = 0;
            synchronized (lock) {
                betDone = true;
                lock.notifyAll();
            }
        }
    }

    public void setLabelText(int order) {
        playerName.setText("Player" + order);
    }
}
