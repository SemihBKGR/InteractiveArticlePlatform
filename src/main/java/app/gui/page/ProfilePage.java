package app.gui.page;

import app.gui.panel.ProfileInformationPanel;

import javax.swing.*;

public class ProfilePage {

    private JPanel panel;
    private JPanel leftPanel;

    private ProfileInformationPanel informationPanel;

    public JPanel getPanel() {
        return panel;
    }

    public ProfileInformationPanel getInformationPanel() {
        return informationPanel;
    }


    private void createUIComponents() {

        informationPanel=new ProfileInformationPanel();
        leftPanel=informationPanel.getPanel();

    }
}
