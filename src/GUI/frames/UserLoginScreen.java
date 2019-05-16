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
    private JPanel loginPanel;
    private JLabel label;

    public UserLoginScreen(Object lock) {
        super(lock);
        this.buttonPanel.setVisible(false);
        loginPanel = new JPanel();
        loginPanel.setBackground(MyColors.transparent);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        loginPanel.setBorder(new RoundedBorder(10));
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

        label = new JLabel("WAITING FOR PLAYERS...");
        label.setForeground(Color.BLACK);
        label.setFont(MyFonts.COURIER);
        label.setVisible(false);


        loginPanel.add(lblUsername);
        loginPanel.add(txtUsername);
      //  loginPanel.add(lblPassword);
       // loginPanel.add(txtPassword);

        this.backgroundPanel.add(loginPanel);
        this.backgroundPanel.add(btnLogin);
        this.backgroundPanel.add(label);

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

    public void dispose() {
        dispose();
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLoginPanelVisibility(boolean visibility) {
        loginPanel.setVisible(visibility);
        btnLogin.setVisible(visibility);
        label.setVisible(!visibility);
    }
}
