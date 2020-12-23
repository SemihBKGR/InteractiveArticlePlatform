package app.gui.panel;

import app.util.Resources;
import com.bulenkov.iconloader.util.Scalr;
import core.DataHandler;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
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
    private volatile Information information;

    private final Thread buttonControlThread;

    private final Color saveButtonInactiveColor =new Color(187,187,187);
    private final Color saveButtonActiveColor=Color.WHITE;

    byte newImage[];

    public ProfileInformationPanel() {

        saveButtonClickable = new AtomicBoolean(false);

        saveButton.setForeground(saveButtonInactiveColor);

        imageLabel.setBorder(new LineBorder(Color.BLACK,5));

        newImage=null;

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
                jfc.setDialogTitle("Choose image");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    try {
                        BufferedImage image =
                                Scalr.resize(ImageIO.read(selectedFile), Scalr.Method.BALANCED, 300,300);
                        imageLabel.setIcon(new ImageIcon(image));
                        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                        ImageOutputStream imageOutputStream=ImageIO.createImageOutputStream(byteArrayOutputStream);
                        ImageIO.write(image,"jpg",imageOutputStream);
                        newImage=byteArrayOutputStream.toByteArray();
                        information.setImage(newImage);
                        setSaveButtonActiveness(true);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        buttonControlThread=new Thread(()->{
            while(!Thread.interrupted()) {

                if (
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
        information=user.getInformation();
        setInformation();
        buttonControlThread.start();
    }

    private void setSaveButtonActiveness(boolean active){
        saveButtonClickable.set(active);
        saveButton.setForeground(active?saveButtonActiveColor:saveButtonInactiveColor);
    }

    private void setInformation(){

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

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
        if(!information.equals(newInformation)){
            user.setInformation(newInformation);
            this.information=newInformation;
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

        newInformation.setId(information.getId());
        newInformation.setName(nameField.getText());
        newInformation.setSurname(surnameField.getText());
        newInformation.setAddress(addressField.getText());
        newInformation.setCompany(companyField.getText());
        newInformation.setBiography(biographyArea.getText());

        if(newImage!=null){
            newInformation.setImage(newImage);
        }

        return information.equals(newInformation)?null:newInformation;

    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getSurnameLabel() {
        return surnameLabel;
    }

    public JLabel getBiographyLabel() {
        return biographyLabel;
    }

    public JLabel getCompanyLabel() {
        return companyLabel;
    }

    public JLabel getAddressLabel() {
        return addressLabel;
    }

    public JLabel getSaveInformationLabel() {
        return saveInformationLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public JLabel getPhoneLabel() {
        return phoneLabel;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getSurnameField() {
        return surnameField;
    }

    public JTextField getCompanyField() {
        return companyField;
    }

    public JTextField getAddressField() {
        return addressField;
    }

    public JTextArea getBiographyArea() {
        return biographyArea;
    }

    public JFormattedTextField getPhoneField() {
        return phoneField;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}
