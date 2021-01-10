package app.gui.page;

import app.Contracts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage {

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

            @Override
            public void mouseEntered(MouseEvent e) {
                usernameLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                usernameLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

        emailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                emailField.requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                emailLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
               emailLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

        passwordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                passwordField.requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                passwordLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passwordLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

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
