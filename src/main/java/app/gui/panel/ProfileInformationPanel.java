package app.gui.panel;


import app.util.Resources;
import core.DataHandler;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProfileInformationPanel {

    private JPanel panel;

    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel biographyLabel;
    private JLabel companyLabel;
    private JLabel addressLabel;
    private JLabel saveInformationLabel;
    private JLabel emailLabel;
    private JLabel usernameLabel;
    private JLabel imageLabel;
    private JLabel phoneLabel;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField companyField;
    private JTextField addressField;
    private JTextArea biographyArea;
    private JFormattedTextField phoneField;

    private JButton saveButton;

    private final AtomicBoolean saveButtonClickable;

    private User user;

    private final Thread buttonControlThread;

    private final Color saveButtonInactiveColor =new Color(187,187,187);
    private final Color saveButtonActiveColor=Color.WHITE;

    private byte[] newImage;

    public ProfileInformationPanel() {

        panel.requestFocus();

        saveButtonClickable = new AtomicBoolean(false);

        newImage=null;

        saveButton.setForeground(saveButtonInactiveColor);

        imageLabel.setBorder(new LineBorder(Color.BLACK,5));
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (saveButtonClickable.get()) {
                    saveButtonClickable.set(false);
                    saveButton.setForeground(saveButtonInactiveColor);
                    saveInformation();
                }
                saveInformationLabel.setText("");
            }
        });

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Choose Image");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setAcceptAllFileFilterUsed(false);
                jfc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
                int returnValue = jfc.showOpenDialog(ProfileInformationPanel.this.panel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    String imageName=selectedFile.getName();
                    String extension=imageName.substring(imageName.lastIndexOf(".")+1,imageName.length());
                    try {
                        BufferedImage image = Resources.resizeImage(ImageIO.read(selectedFile));
                        imageLabel.setIcon(new ImageIcon(image));
                        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                        ImageOutputStream imageOutputStream=ImageIO.createImageOutputStream(byteArrayOutputStream);
                        ImageIO.write(image,extension,imageOutputStream);
                        newImage=byteArrayOutputStream.toByteArray();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        buttonControlThread=new Thread(()->{
            while(!Thread.interrupted()) {

                Information information=user.getInformation();

                if (newImage!=null ||
                    (!nameField.getText().equals(information.getName()!=null?information.getName():"")) ||
                    (!surnameField.getText().equals(information.getSurname()!=null?information.getSurname():"")) ||
                    (!addressField.getText().equals(information.getAddress()!=null?information.getAddress():"")) ||
                    (!biographyArea.getText().equals(information.getBiography()!=null?information.getBiography():"")) ||
                    (!companyField.getText().equals(information.getCompany()!=null?information.getCompany():"")) ||
                    (!phoneField.getText().equals(information.getPhone()!=null?information.getPhone():""))
                ) {
                    setSaveButtonActiveness(true);

                } else {
                    setSaveButtonActiveness(false);
                }

            }
        });

        buttonControlThread.setDaemon(true);

    }

    public void loadAndStartPanel(User user){
        this.user=user;
        setInformationText();
        buttonControlThread.start();
    }

    public void interrupt(){
        buttonControlThread.interrupt();
    }

    private void setSaveButtonActiveness(boolean active){
        saveButtonClickable.set(active);
        saveButton.setForeground(active?saveButtonActiveColor:saveButtonInactiveColor);
    }

    private void setInformationText(){

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

        Information information=user.getInformation();

        if(information.getImage()!=null){

            ByteArrayInputStream imageStream=new ByteArrayInputStream(information.getImage());
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

        nameField.setText(information.getName()!=null?information.getName():"");
        surnameField.setText(information.getSurname()!=null?information.getSurname():"");
        addressField.setText(information.getAddress()!=null?information.getAddress():"");
        companyField.setText(information.getCompany()!=null?information.getCompany():"");
        biographyArea.setText(information.getBiography()!=null?information.getBiography():"");

    }

    private void loadDefaultImage(){
        imageLabel.setIcon(Resources.defaultImageIcon);
    }

    private void saveInformation(){
        Information newInformation=generateNewInformation();
        if(!user.getInformation().equals(newInformation)){
            user.setInformation(newInformation);
            DataHandler.getDataHandler().informationSaveAsync(user, new DataListener<Information>() {
                @Override
                public void onResult(ApiResponse<Information> response) {
                    saveInformationLabel.setText(response.getMessage());
                }
            });
        }

    }

    private Information generateNewInformation(){

        Information newInformation=new Information();

        newInformation.setId(user.getInformation().getId());
        newInformation.setName(nameField.getText());
        newInformation.setSurname(surnameField.getText());
        newInformation.setAddress(addressField.getText());
        newInformation.setCompany(companyField.getText());
        newInformation.setBiography(biographyArea.getText());

        if(newImage!=null){
            newInformation.setImage(newImage);
            newImage=null;
        }

        return newInformation;

    }

    public JPanel getPanel() {
        return panel;
    }



}
