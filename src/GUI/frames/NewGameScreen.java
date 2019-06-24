package GUI.frames;

import GUI.panels.ImagePanel;
import finals.MyColors;
import game_management.game.GameType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class NewGameScreen implements ActionListener {
    JFrame frame = new JFrame();
    ImagePanel backgroundPanel;
    private JButton startControlledGameButton;
    private JButton startMultiplayerGameButton;
    private JButton startLocalGameButton;
    private JButton startPlayerVSEasyGameButton;
    private JButton backButton;
    private JButton startSimulatedGameButton;
    JPanel buttonPanel;
    JPanel buttonPanelLocal;
    private boolean gameChosen;
    final Object lock;
    private GameType gameType;

    public NewGameScreen(Object lock) {
        frame.setTitle("Briscola chiamata");
        frame.setSize(1000,1200);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        backgroundPanel = new ImagePanel(addIcon("resources/tableBackground.jpg"));
        backgroundPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        backgroundPanel.setSize(frame.getSize());
        backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER,300,20));

        buttonPanel = new JPanel();
        buttonPanel.setBackground(MyColors.TRANSPARENT);
        buttonPanel.setPreferredSize(new Dimension(300,400));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,300,20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50,1,0,0));

        buttonPanelLocal = new JPanel();
        buttonPanelLocal.setBackground(MyColors.TRANSPARENT);
        buttonPanelLocal.setPreferredSize(new Dimension(300,400));
        buttonPanelLocal.setLayout(new FlowLayout(FlowLayout.CENTER,300,20));
        buttonPanelLocal.setBorder(BorderFactory.createEmptyBorder(50,1,0,0));
        buttonPanelLocal.setVisible(false);

        startControlledGameButton = new JButton("Start Controlled Game");
        startControlledGameButton.setBackground(Color.orange);
        startControlledGameButton.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        startControlledGameButton.setBorder(new RoundedBorder(10));
        startControlledGameButton.addActionListener(this);

        startSimulatedGameButton = new JButton("Start Simulated Game");
        startSimulatedGameButton.setBackground(Color.orange);
        startSimulatedGameButton.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        startSimulatedGameButton.setBorder(new RoundedBorder(10));
        startSimulatedGameButton.addActionListener(this);

        startPlayerVSEasyGameButton = new JButton("Start Easy");
        startPlayerVSEasyGameButton.setBackground(Color.orange);
        startPlayerVSEasyGameButton.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        startPlayerVSEasyGameButton.setBorder(new RoundedBorder(10));
        startPlayerVSEasyGameButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.setBackground(Color.orange);
        backButton.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        backButton.setBorder(new RoundedBorder(10));
        backButton.addActionListener(this);

        startMultiplayerGameButton = new JButton("Start Multiplayer Game");
        startMultiplayerGameButton.setBackground(Color.orange);
        startMultiplayerGameButton.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        startMultiplayerGameButton.setBorder(new RoundedBorder(10));
        startMultiplayerGameButton.addActionListener(this);

        startLocalGameButton = new JButton("Start Local Game");
        startLocalGameButton.setBackground(Color.orange);
        startLocalGameButton.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        startLocalGameButton.setBorder(new RoundedBorder(10));
        startLocalGameButton.addActionListener(this);

       // buttonPanel.add(startSimulatedGameButton);
        //buttonPanel.add(startPlayerVSEasyGameButton);
        buttonPanel.add(startLocalGameButton);
        //buttonPanel.add(startControlledGameButton);
        buttonPanel.add(startMultiplayerGameButton);

        buttonPanelLocal.add(startPlayerVSEasyGameButton);
        buttonPanelLocal.add(backButton);


        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon(addIcon("resources/logo.png")));

        backgroundPanel.add(logo);
        backgroundPanel.add(buttonPanel);
        backgroundPanel.add(buttonPanelLocal);

        frame.add(backgroundPanel);

        gameChosen = false;
        this.lock = lock;

    }

    public GameType getGameType() {
        return gameType;
    }

    public boolean isGameChosen() {
        return gameChosen;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(startLocalGameButton)) {
           buttonPanel.setVisible(false);
            buttonPanelLocal.setVisible(true);
            this.frame.repaint();
            this.frame.revalidate();
        }
        else if(e.getSource() == backButton) {
            buttonPanel.setVisible(true);
            buttonPanelLocal.setVisible(false);
            this.frame.repaint();
            this.frame.revalidate();
        }
        else if(e.getSource() == startControlledGameButton) {
            gameType = GameType.CONTROLLED;
            runGame();
        }
        else if(e.getSource() == startSimulatedGameButton) {
            gameType = GameType.SIMULATED;
            runGame();
        }
        else if(e.getSource() == startPlayerVSEasyGameButton) {
            gameType = GameType.EASY;
            runGame();
        }
        else if(e.getSource() == startMultiplayerGameButton) {
            gameType = GameType.MULTIPLAYER;
            runGame();
        }
    }

    private void runGame() {
        frame.setVisible(false);
        synchronized (lock) {
            gameChosen = true;
            lock.notifyAll();
        }
        frame.dispose();
    }


        private Image addIcon(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
            return findImage(resource);
    }

    public static Image findImage(URL resource) {
        BufferedImage body = null;
        try {
            assert resource != null;
            body = ImageIO.read( resource );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public JFrame getFrame() {
        return frame;
    }

     static class RoundedBorder implements Border {

        private int radius;


        RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }


        public boolean isBorderOpaque() {
            return true;
        }


        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}
