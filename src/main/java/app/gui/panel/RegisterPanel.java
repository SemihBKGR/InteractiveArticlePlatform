package app.gui.panel;

import javax.swing.*;

public class RegisterPanel{

    private JPanel panel;
    private JTextField usernameField;
    private JTextField emailField;
    private JLabel registerLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel usernameWarnLabel;
    private JLabel emailWarnLabel;
    private JLabel passwordWarnLabel;
    private JButton registerButton;
    private JLabel registerInfoLabel;
    private JButton loginButton;

    public JPanel getPanel() {
        return panel;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JLabel getRegisterLabel() {
        return registerLabel;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

    public JLabel getPasswordLabel() {
        return passwordLabel;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JLabel getUsernameWarnLabel() {
        return usernameWarnLabel;
    }

    public JLabel getEmailWarnLabel() {
        return emailWarnLabel;
    }

    public JLabel getPasswordWarnLabel() {
        return passwordWarnLabel;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JLabel getRegisterInfoLabel() {
        return registerInfoLabel;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
