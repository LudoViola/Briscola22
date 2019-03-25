package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ButtonCardImage extends JButton {

    private Image image;

    public ButtonCardImage(Image image) {
        this.image = image;
        setBorder(new EmptyBorder(0,0,0,0));
        setOpaque(false);
        setIcon(new ImageIcon(image));
    }


}
