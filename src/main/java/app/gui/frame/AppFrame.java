package app.gui.frame;

import app.gui.panel.ProfilePanel;
import app.util.Resources;
import core.DataHandler;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import javax.xml.crypto.Data;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static app.gui.frame.Contracts.FRAME_TITLE;

public class AppFrame extends JFrame{

    private JPanel panel;
    private JPanel centerPanel;

    private final DataHandler dataHandler;

    private final ProfilePanel profilePanel;

    public AppFrame(){

        super(FRAME_TITLE);
        setIconImage(Resources.getImageIcon("article.png").getImage());
        setMinimumSize(new Dimension(1000,750));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dataHandler= DataHandler.getDataHandler();

        add(panel);

        profilePanel=new ProfilePanel();

        centerPanel.add(profilePanel.getPanel(),"profile");

        //Load profile information
        dataHandler.getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                profilePanel.setProfile(response.getData());
            }
        });

        setComponentsListener();

    }




    private void setComponentsListener(){

        profilePanel.getSaveButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dataHandler.informationSaveAsync(profilePanel.generateInformation(), new DataListener<Information>() {
                    @Override
                    public void onResult(ApiResponse<Information> response) {
                        //TODO set label message
                    }
                });
            }
        });

    }

}
