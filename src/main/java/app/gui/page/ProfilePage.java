package app.gui.page;

import app.gui.panel.InformationPanel;
import app.gui.panel.UserPanel;

import javax.swing.*;

public class ProfilePage {
    private JPanel panel;
    private JPanel leftPanel;
    private JPanel rightPanel;

    private InformationPanel informationPanel;
    private UserPanel userPanel;

    public JPanel getPanel() {
        return panel;
    }

    public InformationPanel getInformationPanel() {
        return informationPanel;
    }

    public UserPanel getUserPanel() {
        return userPanel;
    }

    private void createUIComponents() {

        informationPanel=new InformationPanel();
        userPanel=new UserPanel();

        leftPanel=informationPanel.getPanel();
        rightPanel=userPanel.getPanel();
    }
}
