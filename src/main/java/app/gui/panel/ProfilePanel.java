package app.gui.panel;

import core.entity.Information;
import core.entity.User;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ProfilePanel {

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
    private JTextField usernameField;
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel profileLabel;

    public ProfilePanel() {

        usernameField.setEditable(false);
        emailField.setEditable(false);

        saveButton.setVisible(false);

        setFieldsListener();

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

    public JLabel getImageLabel() {
        return imageLabel;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JLabel getEmailLabel() {
        return emailLabel;
    }

    public void setFieldsListener() {

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                saveButton.setVisible(true);
            }
        };

        nameField.addKeyListener(keyAdapter);
        surnameField.addKeyListener(keyAdapter);
        addressField.addKeyListener(keyAdapter);
        companyField.addKeyListener(keyAdapter);
        biographyArea.addKeyListener(keyAdapter);

    }

    public void setProfile(User user){

        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());

        Information information=user.getInformation();

        nameField.setText(information.getName()!=null?information.getName():"");
        surnameField.setText(information.getSurname()!=null?information.getSurname():"");
        addressField.setText(information.getAddress()!=null?information.getAddress():"");
        companyField.setText(information.getCompany()!=null?information.getCompany():"");
        biographyArea.setText(information.getBiography()!=null?information.getBiography():"");

    }

    public Information generateInformation(){

        Information information=new Information();

        information.setName(nameField.getText());
        information.setSurname(surnameField.getText());
        information.setAddress(addressField.getText());
        information.setCompany(companyField.getText());
        information.setBiography(getBiographyArea().getText());

        return information;

    }



}
