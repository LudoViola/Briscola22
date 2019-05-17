package GUI.panels;

import GUI.buttons.ButtonCardImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CardPanel extends JPanel {

    Image image;
    CardLayout cardLayout;
    ButtonCardImage buttonCardImageUp;


    public CardPanel(Image image) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        buttonCardImageUp = new ButtonCardImage(image);
        buttonCardImageUp.setBackground(new Color(95,54,0));


        URL resource = getClass().getClassLoader().getResource("resources/card_images/retro.png");
        BufferedImage body = null;
        try {
            assert resource != null;
            body = ImageIO.read( resource );
        } catch (IOException e) {
            e.printStackTrace();
        }

        ButtonCardImage buttonCardImageDown = new ButtonCardImage(body);

        add(buttonCardImageUp);
        add(buttonCardImageDown);

    }
    public void setActionListener(ActionListener listener) {
        buttonCardImageUp.setActionListener(listener);
    }
    public void removeActionListener(ActionListener listener) {
        buttonCardImageUp.removeActionListener(listener);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }

    public void turn() {
        cardLayout.next(this);
    }
}
