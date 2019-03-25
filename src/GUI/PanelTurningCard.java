package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PanelTurningCard extends JPanel implements ActionListener {

    Image image;
    CardLayout cardLayout;

    public PanelTurningCard(Image image) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        ButtonCardImage buttonCardImageUp = new ButtonCardImage(image);

        URL resource = getClass().getClassLoader().getResource( "card_images/retro.png" );
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

    @Override
    public void actionPerformed(ActionEvent e) {
        cardLayout.next(this);
    }
}
