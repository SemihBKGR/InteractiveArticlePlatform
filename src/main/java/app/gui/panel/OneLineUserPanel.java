package app.gui.panel;

import app.util.Paged;
import app.util.Resources;
import core.entity.User;
import core.entity.superficial.SuperficialUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class OneLineUserPanel {
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JPanel panel;
    private JLabel articleCountLabel;
    private JLabel contributorCountLabel;

    private User user;
    private SuperficialUser superficialUser;

    public OneLineUserPanel(User user, Paged paged){

        if(user.getInformation().getImage()!=null && user.getInformation().getImage().length>0){
            ByteArrayInputStream imageStream=new ByteArrayInputStream(user.getInformation().getImage());
            try {
                ImageIcon imageIcon=new ImageIcon(ImageIO.read(imageStream));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
                loadDefaultImage();
            }
        }else{
            loadDefaultImage();
        }

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        articleCountLabel.setText("Article : "+user.getOwnArticles().size());
        contributorCountLabel.setText("Contributor : "+user.getContributorArticle().size());

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paged.changePage(ButtonPanel.ActiveButton.menu.name(),user);
            }
        });

    }

    private void loadDefaultImage(){
        imageLabel.setIcon(Resources.defaultImageIcon);
    }


    public JPanel getPanel() {
        return panel;
    }
}
