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
    private JButton helpButton;
    private JButton startPlayerVSEasyGameButton;
    private JButton backButton;
    private JButton startSimulatedGameButton;
    JPanel buttonPanel;
    JPanel buttonPanelLocal;
    private boolean gameChosen;
    final Object lock;
    private GameType gameType;

    public NewGameScreen(Object lock) {
        frame.setResizable(false);
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
        setButton(startControlledGameButton);

        startSimulatedGameButton = new JButton("Start Simulated Game");
        setButton(startSimulatedGameButton);

        startPlayerVSEasyGameButton = new JButton("Start Easy");
        setButton(startPlayerVSEasyGameButton);

        backButton = new JButton("Back");
        setButton(backButton);

        startMultiplayerGameButton = new JButton("Start Multiplayer Game");
        setButton(startMultiplayerGameButton);

        startLocalGameButton = new JButton("Start Local Game");
        setButton(startLocalGameButton);

        helpButton = new JButton("Help");
        setButton(helpButton);

        buttonPanel.add(startLocalGameButton);
        buttonPanel.add(startMultiplayerGameButton);
        buttonPanel.add(helpButton);

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
        else if(e.getSource().equals(helpButton)) {
            try {
                openRules();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.frame,"No internet Connection", "Can't Help you",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openRules() throws IOException {
        JEditorPane website = new JEditorPane("http://briscola.altervista.org/nuovosito/regolamento.htm");
        website.setEditable(false);
        JFrame frame = new JFrame("Rules");
        frame.add(new JScrollPane(website));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
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

    private void setButton(JButton button)  {
        button.setBackground(Color.orange);
        button.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        button.setBorder(new RoundedBorder(10));
        button.addActionListener(this);
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
