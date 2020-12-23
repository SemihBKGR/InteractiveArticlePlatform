package app.gui.page;

import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPage {
    
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
    private JCheckBox rememberMeCheckBox;

    public JPanel getPanel() {
        return panel;
    }

    public void addMouseListeners(){

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });

        usernameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                usernameField.requestFocusInWindow();
            }
        });

        passwordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                passwordField.requestFocusInWindow();
            }
        });

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

    public JCheckBox getRememberMeCheckBox() {
        return rememberMeCheckBox;
    }

}
