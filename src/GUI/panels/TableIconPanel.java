package GUI.panels;

import finals.MyColors;
import finals.Visibility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TableIconPanel extends JPanel {
    private JLabel playerName;
    private JLabel turnPointer;

    public TableIconPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50,0,50,0));
        setBackground(MyColors.TRANSPARENT);

        JLabel image = new JLabel();
        URL resource = getClass().getClassLoader().getResource("resources/user.png");
        BufferedImage body = null;
        try {
            assert resource != null;
            body = ImageIO.read( resource );
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert body != null;
        image.setIcon(new ImageIcon(body));
        playerName = new JLabel();
        turnPointer = new JLabel("YOUR TURN");
        turnPointer.setVisible(Visibility.INVISIBLE);
        turnPointer.setForeground(Color.GREEN);

        add(image,BorderLayout.CENTER);
        add(playerName,BorderLayout.PAGE_START);
        add(turnPointer,BorderLayout.PAGE_END);
    }

    public void setPlayerName(String name) {
        playerName.setText(name);
    }

    public void setTurnPointer(boolean visibility) {
        turnPointer.setVisible(visibility);
    }

    public JLabel getPlayerName() {
        return playerName;
    }
}
