package app.gui.panel;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel {
    
    private JPanel panel;
    private JLabel usernameLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel passwordWarnLabel;
    private JLabel usernameWarnLabel;
    private JButton registerButton;
    private JLabel loginInfoLabel;

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JLabel getLoginLabel() {
        return loginLabel;
    }

    public JLabel getPasswordLabel() {
        return passwordLabel;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JLabel getPasswordWarnLabel() {
        return passwordWarnLabel;
    }

    public JLabel getUsernameWarnLabel() {
        return usernameWarnLabel;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JLabel getLoginInfoLabel() {
        return loginInfoLabel;
    }

}
