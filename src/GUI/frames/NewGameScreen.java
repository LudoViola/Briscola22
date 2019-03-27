package GUI.frames;

import GUI.panels.ImagePanel;
import finals.MyColors;
import game.GameManagement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class NewGameScreen implements ActionListener {
    JFrame frame = new JFrame();
    ImagePanel backgroundPanel;
    private JLabel logo;
    private GameManagement game;
    private JButton startGame;

    public NewGameScreen() {

        Runnable r = () -> loadGame();

        new Thread(r).start();

        loadGame();

        frame.setTitle("Briscola in 5");
        frame.setSize(1000,800);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        backgroundPanel = new ImagePanel(addIcon("resources/backGround.jpg"));
        backgroundPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        backgroundPanel.setSize(frame.getSize());
        backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER,300,40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(MyColors.transparent);
        buttonPanel.setPreferredSize(new Dimension(100,120));
        buttonPanel.setLayout(new GridLayout(1,1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(1,1,0,0));

        startGame = new JButton("Start Game");
        startGame.setBackground(Color.orange);
        startGame.addActionListener(this);

        buttonPanel.add(startGame);

        logo = new JLabel();
        logo.setIcon(new ImageIcon(addIcon("resources/logo.png")));

        backgroundPanel.add(logo);

        backgroundPanel.add(buttonPanel);

        frame.add(backgroundPanel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startGame) {
            game.startGame();
            frame.setVisible(false);
            frame.dispose();
        }
    }

    private void loadGame() {
        game = new GameManagement();
    }

    private Image addIcon(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
        Image body = findImage(resource);
        return body;


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
}
