package app.gui.frame;

import app.gui.page.ProfilePage;
import app.gui.panel.InformationPanel;
import app.util.Resources;
import core.DataHandler;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static app.gui.frame.Contracts.FRAME_TITLE;

public class AppFrame extends JFrame{

    private JPanel panel;
    private JPanel centerPanel;

    private User me;

    private final DataHandler dataHandler;

    private final ProfilePage profilePage;

    public AppFrame(){

        super(FRAME_TITLE);
        setIconImage(Resources.getImageIcon("article.png").getImage());
        setMinimumSize(new Dimension(1000,750));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dataHandler= DataHandler.getDataHandler();

        add(panel);

        profilePage=new ProfilePage();

        centerPanel.add(profilePage.getPanel(),"profile");


        //Load profile information
        dataHandler.getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                me=response.getData();
                profilePage.getInformationPanel().setInformationAndStart(response.getData());
                profilePage.getUserPanel().setUser(response.getData());
            }
        });

        setComponentsListener();

    }


    private void setComponentsListener(){



    }

}
