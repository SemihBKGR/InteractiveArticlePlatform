package app.gui.panel;

import app.util.Resources;
import core.entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class OneLineUserPanel {
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JPanel panel;

    public OneLineUserPanel(User user){

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

    }

    private void loadDefaultImage(){
        imageLabel.setIcon(Resources.defaultImageIcon);
    }


    public JPanel getPanel() {
        return panel;
    }
}
