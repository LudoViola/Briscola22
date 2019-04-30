package GUI.frames;

import GUI.buttons.ButtonCardImage;
import GUI.panels.*;
import card_management.Card;
import finals.MyColors;
import finals.MyFonts;
import game_management.GameType;
import game_management.players.ControlledPlayer;
import card_management.Table;
import game_management.players.Player;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;


public class GameScreen extends JFrame implements ActionListener {

    private JPanel backgroundPanel;
    private JPanel playerCardZone;
    private JPanel tablePanel;
    private JPanel innerTablePanel;
    private CardsGroupPanel cardsContainer;
    private TableCardPanel tableCards;
    private LogPanel logPanel;
    private Player currentPlayer;
    private JLabel playerName;
    private JButton buttonBet;
    private JButton exitButton;
    private boolean betDone;
    private boolean turnDone;
    private boolean gameEnded;
    private boolean listenerEnabled;
    private int bet;
    private JButton buttonPass;
    private JSpinner betSpinner;
    private final Object lock;
    private String imageString;
    private Table table;
    private int higherBet;
    private SpinnerNumberModel model;
    private JPanel innerRightPanel;
    JPanel innerLeftPanel;
    private ArrayList<TableIconPanel> iconPanels;
    private GameType gameType;

    private final int MAX_WIDTH = 1450;


    public GameScreen(Player firstPlayer, Object lock) throws HeadlessException {

        /*setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); //$NON-NLS-1$
        getRootPane().getActionMap().put("Cancel", new AbstractAction(){ //$NON-NLS-1$
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        */


        setTitle("Briscola in 5 Partita Simulata");
        setSize(MAX_WIDTH,800);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        backgroundPanel = new JPanel();
        backgroundPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        backgroundPanel.setPreferredSize(getSize());
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(MyColors.brown);
        rightPanel.setPreferredSize(new Dimension(MAX_WIDTH/7,1000));

        model = new SpinnerNumberModel(61,61,120,2);
        betSpinner = new JSpinner(model);
        betSpinner.setPreferredSize(new Dimension(MAX_WIDTH/14,MAX_WIDTH/14));
        betSpinner.setBackground(MyColors.brown);
        betSpinner.setForeground(Color.BLACK);
        betSpinner.getComponent(0).setBackground(MyColors.brown);
        betSpinner.getComponent(1).setBackground(MyColors.brown);
        betSpinner.getComponent(2).setBackground(MyColors.brown);
        betSpinner.getComponent(0).setForeground(MyColors.brown);
        betSpinner.getComponent(1).setForeground(MyColors.brown);
        betSpinner.getComponent(2).setForeground(MyColors.brown);
        JSpinner.NumberEditor jsEditor = (JSpinner.NumberEditor) betSpinner.getEditor();
        jsEditor.getTextField().setBackground(MyColors.brown);
        jsEditor.getTextField().setForeground(Color.BLACK);
        jsEditor.getTextField().setFont(MyFonts.COURIER);
        JComponent comp = betSpinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        betSpinner.addChangeListener(e -> {
            if((int)betSpinner.getValue()>79) {
               model.setStepSize(1);
            }
            else {
                model.setStepSize(2);
            }
        });


        JPanel betPanel = new JPanel();
        betPanel.setLayout(new GridLayout(4,1));
        betPanel.setBackground(MyColors.brown);
        betPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        playerName = new JLabel("Player0");
        playerName.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        playerName.setBackground(MyColors.brown);
        playerName.setForeground(Color.BLACK);
        playerName.setFont(new Font("Courier",Font.BOLD,18));

        buttonBet = new JButton("BET");
        buttonBet.setBackground(MyColors.brown);
        buttonBet.setForeground(Color.BLACK);
        buttonBet.setFont(new Font("Courier",Font.BOLD,18));
        buttonBet.addActionListener(this);
        buttonBet.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        buttonPass = new JButton("PASS");
        buttonPass.setActionCommand("PASS");
        buttonPass.setForeground(Color.BLACK);
        buttonPass.setFont(new Font("Courier",Font.BOLD,18));
        buttonPass.setBackground(MyColors.brown);
        buttonPass.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        buttonPass.addActionListener(this);

        rightPanel.add(betPanel,BorderLayout.CENTER);
        betPanel.add(playerName);
        betPanel.add(buttonBet);
        betPanel.add(betSpinner);
        betPanel.add(buttonPass);

        //URL resource = getClass().getClassLoader().getResource( "resources/tableBackground.jpg" );
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        tablePanel.setBackground(Color.GREEN);

        innerTablePanel = new JPanel();
        innerTablePanel.setBackground(Color.GREEN);

        innerRightPanel = new JPanel();
        innerRightPanel.setLayout(new GridLayout(2,1));
        innerRightPanel.setBorder(BorderFactory.createEmptyBorder(0,100,0,0));
        innerRightPanel.setBackground(MyColors.transparent);
        innerRightPanel.setVisible(false);
        iconPanels = new ArrayList<>();

        TableIconPanel panel = new TableIconPanel();
        iconPanels.add(panel);

        TableIconPanel panel1 = new TableIconPanel();
        iconPanels.add(panel1);

        innerRightPanel.add(panel1);
        innerRightPanel.add(panel);

        innerLeftPanel = new JPanel();
        innerLeftPanel.setLayout(new GridLayout(2,1));
        innerLeftPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,100));
        innerLeftPanel.setBackground(MyColors.transparent);
        innerLeftPanel.setVisible(false);

        TableIconPanel panel2 = new TableIconPanel();
        iconPanels.add(panel2);

        TableIconPanel panel3 = new TableIconPanel();
        iconPanels.add(panel3);

        innerLeftPanel.add(panel2);
        innerLeftPanel.add(panel3);
        tableCards = new TableCardPanel();
        innerTablePanel.add(tableCards.getPanel());
        innerTablePanel.setVisible(false);

        tablePanel.add(innerTablePanel,BorderLayout.CENTER);
        tablePanel.add(innerLeftPanel,BorderLayout.WEST);
        tablePanel.add(innerRightPanel,BorderLayout.EAST);

        playerCardZone = new JPanel();
        playerCardZone.setPreferredSize(new Dimension(200,200));
        playerCardZone.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        playerCardZone.setLayout(new FlowLayout(FlowLayout.LEFT));
        playerCardZone.setBackground(Color.WHITE);

        JPanel menuZone = new JPanel();
        menuZone.setPreferredSize(new Dimension(MAX_WIDTH,50));
        menuZone.setLayout(new FlowLayout(FlowLayout.RIGHT));
        menuZone.setBackground(MyColors.brown);

        logPanel = new LogPanel();
        logPanel.setPreferredSize(new Dimension(MAX_WIDTH/2 - 12,200));

        exitButton = new JButton("EXIT Game");
        exitButton.setBackground(Color.orange);
        exitButton.setFont(logPanel.getFont());
        exitButton.setPreferredSize(new Dimension(MAX_WIDTH/6,50));
        exitButton.setBorder(new NewGameScreen.RoundedBorder(10));
        exitButton.addActionListener(this);
        exitButton.setVisible(false);

        menuZone.add(exitButton);

        cardsContainer = new CardsGroupPanel(firstPlayer.getHand());
        cardsContainer.getPanel().setPreferredSize(new Dimension(MAX_WIDTH/2 -12,200));
        playerCardZone.add(cardsContainer.getPanel());
        playerCardZone.add(logPanel);

        backgroundPanel.add(menuZone, BorderLayout.PAGE_START);
        backgroundPanel.add(tablePanel,BorderLayout.CENTER);
        backgroundPanel.add(rightPanel,BorderLayout.LINE_START);
        backgroundPanel.add(playerCardZone, BorderLayout.PAGE_END);


        add(backgroundPanel);

        betDone = false;
        turnDone = false;
        gameEnded = false;
        listenerEnabled = false;
        bet = 0;
        this.lock = lock;

    }

    public void log(String s) {
        logPanel.update(s);
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void diplayBettingWinner(String s) {
        logPanel.update(s);
        //JOptionPane.showMessageDialog(this,s,"The Winner", JOptionPane.INFORMATION_MESSAGE);
    }
    public void diplayScoreBoard( String s) {
        String c = "Classifica\n" + s;
        //JOptionPane.showMessageDialog(this,s,"Classifica", JOptionPane.INFORMATION_MESSAGE);
        logPanel.update(c);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void displayHandWinner(Player p) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s = "Hand Winner:" + p.getPlayerID();
       // JOptionPane.showMessageDialog(this,s,"Hand Winner", JOptionPane.INFORMATION_MESSAGE);
        logPanel.update(s);
        tableCards.update(p);
    }

    public void displayBettingMove(Player p,int bet) {
        String s;
        if(bet ==0) {
            s = p.getPlayerID() +" Pass";
        }
        else {
            s = p.getPlayerID() + " bet " + bet;
        }
        logPanel.update(s);
        //JOptionPane.showMessageDialog(this,s,"BettingMove", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateTableCards(Card card) {
            tableCards.update(card);
            revalidate();
            repaint();
    }


    public void updatePlayerCards(Player player) {
        cardsContainer.update(player.getHand(), player instanceof ControlledPlayer);
        //revalidate();
        //repaint();
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

    public boolean isGameEnded() {
        return gameEnded;
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

    public void addNameOnIcon(ArrayList<Player> players) {
        if(gameType.equals(GameType.SIMULATED) || gameType.equals(GameType.CONTROLLED)) {
            int i = 0;
            for (TableIconPanel panel : iconPanels) {
                panel.setPlayerName(players.get(i + 1).getPlayerID());
                i++;
            }
        }
        else {
            int i = 0;
            for (TableIconPanel panel : iconPanels) {
                if(!(players.get(i)instanceof ControlledPlayer)) {
                    panel.setPlayerName(players.get(i).getPlayerID());
                }
                else {
                    i++;
                    panel.setPlayerName(players.get(i).getPlayerID());
                }
                i++;
            }
        }
    }

    public void showYourTurn(String playerId, boolean visibility) {
        for (TableIconPanel pane:iconPanels) {
            if(pane.getPlayerName().getText().equals(playerId)) {
                pane.setTurnPointer(visibility);
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonBet) {
            bet = (Integer) betSpinner.getValue();
            if(bet > higherBet) {
                synchronized (lock) {
                    updateSpinner();
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
        else if(e.getSource() == exitButton) {
                synchronized (lock) {
                    gameEnded = true;
                    lock.notifyAll();
                }
        }
    }

    public void updateSpinner() {
        model.setMinimum(higherBet);
        model.setValue(higherBet);
        try {
            betSpinner.commitEdit();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    public void setLabelText(Player p) {
        playerName.setText(p.getClass().getSimpleName()+"  " + p.getOrder());
    }

    public void setBetAreaVisibility(boolean b) {
        buttonBet.setVisible(b);
        buttonPass.setVisible(b);
        betSpinner.setVisible(b);
    }

    public void setTableVisibility(boolean b) {
        innerTablePanel.setVisible(b);
        innerLeftPanel.setVisible(b);
        innerRightPanel.setVisible(b);
    }

    public void setExitButtonVisibility(boolean b) {
        exitButton.setVisible(b);
    }

    public void setActionListener() {
        cardsContainer.setActionListener(this);
    }


}
