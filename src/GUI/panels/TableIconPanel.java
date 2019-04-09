package GUI.panels;

import finals.MyColors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TableIconPanel extends JPanel {
    private JLabel image;
    private JLabel playerName;
    private JLabel turnPointer;

    public TableIconPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50,0,50,0));
        setBackground(MyColors.transparent);

        image = new JLabel();
        URL resource = getClass().getClassLoader().getResource("resources/user.png");
        BufferedImage body = null;
        try {
            assert resource != null;
            body = ImageIO.read( resource );
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setIcon(new ImageIcon(body));
        playerName = new JLabel();

        add(image,BorderLayout.CENTER);
        add(playerName,BorderLayout.PAGE_START);
    }

    public void setPlayerName(String name) {
        playerName.setText(name);
    }
}
