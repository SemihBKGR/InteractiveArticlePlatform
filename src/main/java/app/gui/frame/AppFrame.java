package app.gui.frame;

import app.gui.page.ProfilePage;
import app.util.Resources;
import core.DataHandler;
import core.entity.User;

import javax.swing.*;

import java.awt.*;

import static app.Contracts.FRAME_TITLE;


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


        profilePage.start();


    }





}
