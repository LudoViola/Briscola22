package GUI.panels;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {

    private JTextArea logsField;
    private JScrollPane scrollPane;
    private JScrollBar vertical;
    final Font font = new Font("Courier", Font.BOLD,12);
    public LogPanel() {
        setBackground(Color.BLACK);

        logsField = new JTextArea("Game Started");

        logsField.setFont(font);
        logsField.setBackground(Color.BLACK);
        logsField.setForeground(Color.GREEN);

        scrollPane = new JScrollPane(logsField);

        vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);


    }

    @Override
    public Font getFont() {
        return font;
    }

    public void update(String s) {
        String text = logsField.getText() + "\n";
            logsField.setText(text + s);
            logsField.setLineWrap(true);
        logsField.setFont(font);
        logsField.setBackground(Color.BLACK);
        logsField.setForeground(Color.GREEN);
            logsField.setWrapStyleWord(true);
        vertical.setValue( vertical.getMaximum() );

        }

    }
