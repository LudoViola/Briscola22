package GUI.frames;

import finals.MyColors;
import finals.MyFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserLoginScreen extends NewGameScreen {

    private JButton btnLogin;
    private JButton btnCancel;
    private JTextField txtUsername;
    private String username;
    private boolean isLogged;

    public UserLoginScreen(Object lock) {
        super(lock);
        this.buttonPanel.setVisible(false);
        JPanel panel = new JPanel();
        panel.setBackground(MyColors.transparent);
        panel.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        panel.setBorder(new RoundedBorder(10));
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(MyFonts.COURIER);
       // JLabel lblPassword = new JLabel("Password:");
        txtUsername = new JTextField(20);
       // JTextField txtPassword = new JTextField(20);

        btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.orange);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        btnLogin.setBorder(new RoundedBorder(10));
        btnLogin.addActionListener(this);
        btnCancel = new JButton("Cancel");


        panel.add(lblUsername);
        panel.add(txtUsername);
      //  panel.add(lblPassword);
       // panel.add(txtPassword);

        this.backgroundPanel.add(panel);
        this.backgroundPanel.add(btnLogin);

        isLogged = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnLogin && !(txtUsername.getText().equals(""))) {
            username = txtUsername.getText();
            synchronized (lock) {
                isLogged = true;
                lock.notifyAll();
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isLogged() {
        return isLogged;
    }
}
