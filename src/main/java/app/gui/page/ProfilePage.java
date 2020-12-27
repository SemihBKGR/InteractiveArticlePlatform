package app.gui.page;

import app.gui.frame.AppFrame;
import app.gui.frame.TransactionFrame;
import app.gui.panel.ProfileArticlePanel;
import app.gui.panel.ProfileInformationPanel;
import core.DataHandler;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfilePage {

    private JPanel panel;
    private JPanel InformationPanel;

    private JPanel articlePanel;
    private JPanel informationPanel;
    private JButton logoutButton;
    private JButton searchButton;

    private DataHandler dataHandler;

    ProfileInformationPanel profileInformationPanel;
    ProfileArticlePanel profileArticlePanel;

    public ProfilePage(AppFrame appFrame){
        dataHandler=DataHandler.getDataHandler();

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                appFrame.changePage("search");
            }
        });

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profileInformationPanel.interrupt();
                appFrame.dispose();
                new TransactionFrame().setVisible(true);
            }
        });


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
