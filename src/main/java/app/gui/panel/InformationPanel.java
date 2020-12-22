package app.gui.panel;

import core.DataHandler;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class InformationPanel {

    private JPanel panel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel biographyLabel;
    private JLabel companyLabel;
    private JLabel addressLabel;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField companyField;
    private JTextField addressField;
    private JButton saveButton;
    private JTextArea biographyArea;
    private JLabel phoneLabel;
    private JFormattedTextField phoneField;
    private JLabel saveInformationLabel;

    private final AtomicBoolean saveButtonClickable;

    private User user;
    private volatile Information information;

    private final Thread buttonControlThread;

    private final Color saveButtonInActiveColor=new Color(187,187,187);
    private final Color saveButtonActiveColor=Color.WHITE;

    public InformationPanel() {

        saveButton.setEnabled(false);

        saveButtonClickable =new AtomicBoolean(false);
        saveButton.setForeground(saveButtonInActiveColor);

        setFieldsListener();

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
                    saveButtonClickable.set(true);
                    saveButton.setForeground(saveButtonActiveColor);

                } else {
                    saveButtonClickable.set(false);
                    saveButton.setForeground(saveButtonInActiveColor);
                }
            }
        });

    }

    public void setInformationAndStart(User user){

        this.user=user;
        information=user.getInformation();
        setProfile();
        buttonControlThread.start();
        saveButton.setEnabled(true);

    }

    public void setFieldsListener() {

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (saveButtonClickable.get()) {
                    saveButtonClickable.set(false);
                    saveButton.setForeground(saveButtonInActiveColor);
                    saveInformation();
                }
                saveInformationLabel.setText("");
            }
        });

    }

    private void setProfile(){

        nameField.setText(information.getName()!=null?information.getName():"");
        surnameField.setText(information.getSurname()!=null?information.getSurname():"");
        addressField.setText(information.getAddress()!=null?information.getAddress():"");
        companyField.setText(information.getCompany()!=null?information.getCompany():"");
        biographyArea.setText(information.getBiography()!=null?information.getBiography():"");

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

    public JButton getSaveButton() {
        return saveButton;
    }

    public JTextArea getBiographyArea() {
        return biographyArea;
    }

    public JLabel getPhoneLabel() {
        return phoneLabel;
    }

    public JFormattedTextField getPhoneField() {
        return phoneField;
    }


    public Information getInformation() {
        return information;
    }

    public Thread getButtonControlThread() {
        return buttonControlThread;
    }

}
