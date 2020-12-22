package app.gui.panel;


import core.entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class UserPanel {

    private JPanel panel;
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JButton selectButton;

    public UserPanel() {

        selectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Choose profile photo");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    try {
                        ImageIcon imageIcon=new ImageIcon(ImageIO.read(selectedFile).getScaledInstance(300,300, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(imageIcon);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

    }

    public void setUser(User user){
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

}
