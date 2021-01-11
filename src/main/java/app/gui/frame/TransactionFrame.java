package app.gui.frame;

import app.gui.page.LoginPage;
import app.gui.page.RegisterPage;
import app.util.*;
import core.DataHandler;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import static app.Contracts.FRAME_TITLE;


@Slf4j
public class TransactionFrame extends JFrame{

    private DataHandler dataHandler;

    private JPanel panel;
    private JPanel centerPanel;

    private final LoginPage loginPanel;
    private final RegisterPage registerPanel;

    private AtomicBoolean loginButtonClickable;
    private AtomicBoolean registerButtonClickable;

    public TransactionFrame() {

        super(FRAME_TITLE);
        setIconImage(Resources.getImageIcon("article.png").getImage());
        setMinimumSize(new Dimension(400,600));
        setSize(new Dimension(500,750));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dataHandler=DataHandler.getDataHandler();

        loginButtonClickable=new AtomicBoolean(true);
        registerButtonClickable=new AtomicBoolean(true);

        add(panel);

        loginPanel=new LoginPage();
        registerPanel=new RegisterPage();
        loginPanel.addMouseListeners();
        registerPanel.addMouseListeners();

        centerPanel.add(loginPanel.getPanel(),"login");
        centerPanel.add(registerPanel.getPanel(),"register");

        addComponentListener();
        setComponentStateRegardingSetting();

    }

    private void addComponentListener() {

        loginPanel.getLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(loginButtonClickable.get()){
                    loginButtonClickable.set(false);
                    log.info("Login button clicked");
                    login();
                }
            }
        });

        loginPanel.getRegisterButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout)centerPanel.getLayout()).show(centerPanel,"register");
            }
        });

        loginPanel.getRememberMeCheckBox().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Settings.setSetting(Settings.REMEMBER_ME,
                        loginPanel.getRememberMeCheckBox().isSelected());
            }
        });

        registerPanel.getRegisterButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(registerButtonClickable.get()){
                    registerButtonClickable.set(false);
                    log.info("Register transaction started");
                    register();
                }
            }
        });

        registerPanel.getLoginButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout)centerPanel.getLayout()).show(centerPanel,"login");
            }
        });

    }

    private void setComponentStateRegardingSetting(){

        boolean rememberme= TypeConverts.toBoolean
                (Settings.getSetting(Settings.REMEMBER_ME));

        loginPanel.getRememberMeCheckBox().setSelected(rememberme);

        if(rememberme){
            loginPanel.getUsernameField().setText(TypeConverts.toString(Settings.getSetting(Settings.USERNAME)));
            loginPanel.getPasswordField().setText(TypeConverts.toString(Settings.getSetting(Settings.PASSWORD)));
        }


    }

    private void login(){

        loginPanel.getUsernameWarnLabel().setText("");
        loginPanel.getPasswordWarnLabel().setText("");
        loginPanel.getLoginInfoLabel().setText("");

        String username=loginPanel.getUsernameField().getText();
        char[] password=loginPanel.getPasswordField().getPassword();

        boolean allConfirmed=true;

        Confirmation.ConfirmationMessage usernameConfirmationMessage= Confirmation.usernameConfirmation(username);
        if(!usernameConfirmationMessage.isConfirmed()){
            loginPanel.getUsernameWarnLabel().setText(usernameConfirmationMessage.getMessages());
            allConfirmed=false;
        }

        Confirmation.ConfirmationMessage passwordConfirmationMessage=Confirmation.passwordConfirmation(new String(password));
        if(!passwordConfirmationMessage.isConfirmed()){
            loginPanel.getPasswordWarnLabel().setText(passwordConfirmationMessage.getMessages());
            allConfirmed=false;
        }

        if(allConfirmed){

            log.info("Login transaction started.");

            dataHandler.loginAsync(username, new String(password), new DataListener<KeyValue>() {

                @Override
                public void onStart() {
                    loginPanel.getLoginInfoLabel().setText("Processing");
                }

                @Override
                    public void onException(Throwable e) {
                        loginPanel.getLoginInfoLabel().setText("Something went wrong");
                        loginButtonClickable.set(true);
                    }

                    @Override
                    public void onResult(ApiResponse<KeyValue> response) {
                        loginPanel.getLoginInfoLabel().setText(HtmlParse.convertToHtml(response.getMessage()));

                        if(response.isConfirmed()){
                            log.info("Login is success.");
                            dataHandler.addHeader(response.getData());
                            SwingUtilities.invokeLater(()->{
                                new AppFrame().setVisible(true);
                            });
                            dispose();
                        }else{
                            log.info("Login is fail.");
                            loginButtonClickable.set(true);
                        }
                    }
                });

        }else{
            log.info("Login transaction could not bo started.");
            loginButtonClickable.set(true);
        }

        if(TypeConverts.toBoolean(Settings.getSetting(Settings.REMEMBER_ME))) {
            Settings.setSetting(Settings.USERNAME, username);
            Settings.setSetting(Settings.PASSWORD, new String(password));
        }

    }

    private void register(){

        registerPanel.getRegisterInfoLabel().setText("");
        registerPanel.getUsernameWarnLabel().setText("");
        registerPanel.getEmailWarnLabel().setText("");
        registerPanel.getPasswordWarnLabel().setText("");

        String username=registerPanel.getUsernameField().getText();
        String email=registerPanel.getEmailField().getText();
        char[] password=registerPanel.getPasswordField().getPassword();

        boolean allConfirmed=true;

        Confirmation.ConfirmationMessage usernameConfirmationMessage=Confirmation.usernameConfirmation(username);
        if(!usernameConfirmationMessage.isConfirmed()){
            registerPanel.getUsernameWarnLabel().setText(usernameConfirmationMessage.getMessages());
            allConfirmed=false;
        }

        Confirmation.ConfirmationMessage emailConfirmationMessage=Confirmation.emailConfirmation(email);
        if(!emailConfirmationMessage.isConfirmed()){
            registerPanel.getEmailWarnLabel().setText(emailConfirmationMessage.getMessages());
            allConfirmed=false;
        }

        Confirmation.ConfirmationMessage passwordConfirmationMessage=Confirmation.passwordConfirmation(new String(password));
        if(!passwordConfirmationMessage.isConfirmed()){
            registerPanel.getPasswordWarnLabel().setText(passwordConfirmationMessage.getMessages());
            allConfirmed=false;
        }

        if(allConfirmed){

            log.info("Register transaction started.");

            dataHandler.registerAsync(username, email, new String(password), new DataListener<User>() {
                @Override
                public void onStart() {
                    registerPanel.getRegisterInfoLabel().setText("Processing");
                }

                @Override
                public void onException(Throwable e) {
                    registerPanel.getRegisterInfoLabel().setText("Something went wrong");
                    registerButtonClickable.set(true);
                }

                @Override
                public void onResult(ApiResponse<User> response) {
                    registerPanel.getRegisterInfoLabel().setText(HtmlParse.convertToHtml(response.getMessage()));

                    if(response.isConfirmed()){
                        registerPanel.getUsernameField().setText("");
                        registerPanel.getEmailField().setText("");
                        registerPanel.getPasswordField().setText("");
                        log.info("Register is success");
                    }else{
                        log.info("Register is fail");
                    }
                    registerButtonClickable.set(true);

                }
            });

        }else{
            log.info("Register transaction could not bo started.");
            registerButtonClickable.set(true);
        }

    }

}
