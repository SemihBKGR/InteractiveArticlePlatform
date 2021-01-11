package app.gui.panel;

import app.Contracts;
import app.util.Paged;
import app.util.Resources;
import core.entity.User;
import core.entity.superficial.SuperficialUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
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

        imageLabel.setBorder(new LineBorder(Color.BLACK,3));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        usernameLabel.setText("Username : "+user.getUsername());
        emailLabel.setText("Email : "+user.getEmail());
        articleCountLabel.setText("Article Count : "+user.getOwnArticles().size());
        contributorCountLabel.setText("Contribute Count : "+user.getContributorArticle().size());
        loadImage(user.getInformation().getImage());
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                paged.changePage(ButtonPanel.ActiveButton.menu.name(),user);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new LineBorder(Contracts.DEFAULT_BLUE,3));
                usernameLabel.setForeground(Color.WHITE);
                emailLabel.setForeground(Color.WHITE);
                contributorCountLabel.setForeground(Color.WHITE);
                articleCountLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new LineBorder(Contracts.DEFAULT_LIGHT_GRAY));
                usernameLabel.setForeground(Contracts.DEFAULT_WHITE);
                emailLabel.setForeground(Contracts.DEFAULT_WHITE);
                contributorCountLabel.setForeground(Contracts.DEFAULT_WHITE);
                articleCountLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

    }

    private void loadImage(byte[] image){
        if(image!=null){
            ByteArrayInputStream imageStream=new ByteArrayInputStream(image);
            try {
                ImageIcon imageIcon=new ImageIcon(ImageIO.read(imageStream));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
                imageLabel.setIcon(Resources.defaultImageIcon);
            }
        }else{
            imageLabel.setIcon(Resources.defaultImageIcon);
        }
    }


    public JPanel getPanel() {
        return panel;
    }
}
