package app.gui.frame;

import app.gui.panel.LoginPanel;
import app.gui.panel.RegisterPanel;
import app.util.Confirmation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransactionFrame extends JFrame{

    private JPanel panel;
    private JPanel centerPanel;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;

    public TransactionFrame() {

        super("App");
        setMinimumSize(new Dimension(350,500));
        setSize(new Dimension(700,1000));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(panel);

        loginPanel=new LoginPanel();
        registerPanel=new RegisterPanel();

        centerPanel.add(loginPanel.getPanel(),"login");
        centerPanel.add(registerPanel.getPanel(),"register");

        addComponentMouseListener();

    }


    private void addComponentMouseListener() {

        loginPanel.getRegisterButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout)centerPanel.getLayout()).show(centerPanel,"register");
            }
        });

        loginPanel.getLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login();
            }
        });

        registerPanel.getLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout)centerPanel.getLayout()).show(centerPanel,"login");
            }
        });

        registerPanel.getRegisterButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                register();
            }
        });

    }

    private void login(){

        loginPanel.getUsernameWarnLabel().setText("");
        loginPanel.getPasswordWarnLabel().setText("");

        String username=loginPanel.getUsernameField().getText();
        char[] password=loginPanel.getPasswordField().getPassword();

        Confirmation.ConfirmationMessage usernameConfirmationMessage= Confirmation.usernameConfirmation(username);

        if(!usernameConfirmationMessage.isConfirmed()){
            loginPanel.getUsernameWarnLabel().setText(usernameConfirmationMessage.getMessages());
        }

        Confirmation.ConfirmationMessage passwordConfirmationMessage=Confirmation.passwordConfirmation(new String(password));

        if(!passwordConfirmationMessage.isConfirmed()){
            loginPanel.getPasswordWarnLabel().setText(passwordConfirmationMessage.getMessages());
        }


        if(usernameConfirmationMessage.isConfirmed() && passwordConfirmationMessage.isConfirmed()){



        }

    }

    private void register(){



    }

}
