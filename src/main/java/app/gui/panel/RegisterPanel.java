package app.gui.panel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JCheckBox policyCheckBox;

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

        emailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                emailField.requestFocusInWindow();
            }
        });

        passwordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                passwordField.requestFocusInWindow();
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

    public JCheckBox getPolicyCheckBox() {
        return policyCheckBox;
    }

}
