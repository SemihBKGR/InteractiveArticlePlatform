package app.gui.panel;

import app.util.Paged;
import app.util.Resources;
import core.entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class OneLineContributorPanel {

    private JLabel usernameLabel;
    private JButton addButton;
    private JPanel panel;
    private JLabel imageLabel;
    private JPanel innerPanel;

    public OneLineContributorPanel(User user, Paged paged){

        usernameLabel.setText(user.getUsername());
        imageLabel.setBorder(new LineBorder(Color.BLACK,1));
        loadImage(user.getInformation().getImage());
        panel.setBorder(new LineBorder(Color.BLACK,1));
        innerPanel.setBorder(new LineBorder(Color.BLACK,1));

        innerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                innerPanel.setBorder(new LineBorder(Color.GREEN,1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                innerPanel.setBorder(new LineBorder(Color.BLACK,1));
            }
        });

        innerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paged.changePage(ButtonPanel.ActiveButton.menu.name(),user);
            }
        });

    }

    private void loadImage(byte[] image){
        if(image!=null){
            ByteArrayInputStream imageStream=new ByteArrayInputStream(image);
            try {
                ImageIcon imageIcon=new ImageIcon(Resources.resizeSmallSize(ImageIO.read(imageStream)));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
                imageLabel.setIcon(Resources.smallDefaultImageIcon);
            }
        }else{
            imageLabel.setIcon(Resources.smallDefaultImageIcon);
        }
    }

    public JPanel getPanel() {
        return panel;
    }


}
