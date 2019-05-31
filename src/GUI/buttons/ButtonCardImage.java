package GUI.buttons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonCardImage extends JButton {

    public ButtonCardImage(Image image) {
        setBorder(new EmptyBorder(0,0,0,0));
        setOpaque(false);
        setIcon(new ImageIcon(image));
        setActionCommand(image.toString());
    }

    public void setActionListener(ActionListener listener) {
        this.addActionListener(listener);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected()) {
            setBorder(BorderFactory.createEmptyBorder());
        } else {
            setBorder(BorderFactory.createLoweredBevelBorder());
        }
    }


}
