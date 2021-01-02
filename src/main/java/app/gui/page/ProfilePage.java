package app.gui.page;

import app.gui.frame.AppFrame;
import app.gui.frame.TransactionFrame;
import app.gui.panel.ProfileArticlePanel;
import app.gui.panel.ProfileInformationPanel;
import app.util.Paged;
import core.DataHandler;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfilePage {

    //TODO image save bug

    private JPanel panel;
    private JPanel InformationPanel;

    private JPanel articlePanel;
    private JPanel informationPanel;
    private JButton logoutButton;
    private JButton searchButton;

    private DataHandler dataHandler;

    ProfileInformationPanel profileInformationPanel;
    ProfileArticlePanel profileArticlePanel;

    private Paged paged;

    public ProfilePage(Paged paged){
        this.paged=paged;
        dataHandler=DataHandler.getDataHandler();
    }

    public void start(){
        dataHandler.getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                profileInformationPanel.loadAndStartPanel(response.getData());
                profileArticlePanel.loadAndStartPanel(response.getData());
            }
        });
    }

    public void stop(){
        profileInformationPanel.interrupt();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {

        profileInformationPanel=new ProfileInformationPanel();
        profileArticlePanel=new ProfileArticlePanel(paged);

        informationPanel = profileInformationPanel.getPanel();
        articlePanel = profileArticlePanel.getPanel();

    }


}
