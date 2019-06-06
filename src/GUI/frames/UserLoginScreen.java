package GUI.frames;

import finals.MyColors;
import finals.MyFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserLoginScreen extends NewGameScreen {

    private JButton btnLogin;
    private JButton btnSignUp;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtIp;
    private String username;
    private String password;
    private boolean isSigningUp;
    private boolean isLogged;
    private JPanel usernamePanel;
    private JPanel passwordPanel;
    private JPanel buttonsPanel;
    private JPanel ipPanel;
    private JLabel label;

    public UserLoginScreen(Object lock) {
        super(lock);
        this.buttonPanel.setVisible(false);
        usernamePanel = new JPanel();
        usernamePanel.setBackground(MyColors.TRANSPARENT);
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        usernamePanel.setBorder(new RoundedBorder(10));
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(MyFonts.COURIER);
        txtUsername = new JTextField(20);

        passwordPanel = new JPanel();
        passwordPanel.setBackground(MyColors.TRANSPARENT);
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        passwordPanel.setBorder(new RoundedBorder(10));
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setFont(MyFonts.COURIER);
        txtPassword = new JTextField(20);

        ipPanel = new JPanel();
        ipPanel.setBackground(MyColors.TRANSPARENT);
        ipPanel.setBorder(BorderFactory.createEmptyBorder(5,1,5,0));
        ipPanel.setBorder(new RoundedBorder(10));
        JLabel lblIp = new JLabel("Server Ip:");
        lblIp.setForeground(Color.BLACK);
        lblIp.setFont(MyFonts.COURIER);
        // JLabel lblPassword = new JLabel("Password:");
        txtIp = new JTextField(20);
        // JTextField txtPassword = new JTextField(20);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1,2));
        buttonsPanel.setBackground(MyColors.TRANSPARENT);

        btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.orange);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(5,22,5,0));
        btnLogin.setBorder(new RoundedBorder(10));
        btnLogin.addActionListener(this);

        btnSignUp = new JButton("Sign Up");
        btnSignUp.setBackground(Color.orange);
        btnSignUp.setBorder(BorderFactory.createEmptyBorder(5,10,5,15));
        btnSignUp.setBorder(new RoundedBorder(10));
        btnSignUp.addActionListener(this);
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnSignUp);
        label = new JLabel("WAITING FOR PLAYERS...");
        label.setForeground(Color.BLACK);
        label.setFont(MyFonts.COURIER);
        label.setVisible(false);


        usernamePanel.add(lblUsername);
        usernamePanel.add(txtUsername);
        passwordPanel.add(lblPassword);
        passwordPanel.add(txtPassword);
        ipPanel.add(lblIp);
        ipPanel.add(txtIp);

        this.backgroundPanel.add(usernamePanel);
        this.backgroundPanel.add(passwordPanel);
        this.backgroundPanel.add(ipPanel);
        this.backgroundPanel.add(buttonsPanel);
        this.backgroundPanel.add(label);

        isLogged = false;
        isSigningUp = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnLogin && !(txtUsername.getText().equals("")) && !(txtIp.getText().equals("")) && !(txtPassword.getText().equals("")) ) {
            username = txtUsername.getText();
            password = txtPassword.getText();
            synchronized (lock) {
                isLogged = true;
                lock.notifyAll();
            }
        }
        else if((e.getSource() == btnSignUp && !(txtUsername.getText().equals("")) && !(txtIp.getText().equals("")) && !(txtPassword.getText().equals("")) )) {
            username = txtUsername.getText();
            password = txtPassword.getText();
            isLogged = true;
            isSigningUp = true;
            label.setText("WAITING FOR CONFIRM.....");
            synchronized (lock) {
                lock.notifyAll();
            }
        }

    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return txtIp.getText();
    }

    public boolean isSigningUp() {
        return isSigningUp;
    }

    public String getPassword() {
        return password;
    }

    public void dispose() {
        this.frame.dispose();
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLoginPanelVisibility(boolean visibility) {
        usernamePanel.setVisible(visibility);
        ipPanel.setVisible(visibility);
        passwordPanel.setVisible(visibility);
        buttonsPanel.setVisible(visibility);
        label.setVisible(!visibility);
    }
}
