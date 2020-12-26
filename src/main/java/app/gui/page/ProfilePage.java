package app.gui.page;

import app.gui.panel.ProfileArticlePanel;
import app.gui.panel.ProfileInformationPanel;
import core.DataHandler;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;

public class ProfilePage {

    private JPanel panel;
    private JPanel InformationPanel;

    private JPanel articlePanel;
    private JPanel informationPanel;

    private DataHandler dataHandler;

    ProfileInformationPanel profileInformationPanel;
    ProfileArticlePanel profileArticlePanel;

    public ProfilePage(){
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

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {

        profileInformationPanel=new ProfileInformationPanel();
        profileArticlePanel=new ProfileArticlePanel();

        informationPanel = profileInformationPanel.getPanel();
        articlePanel = profileArticlePanel.getPanel();

    }


}
