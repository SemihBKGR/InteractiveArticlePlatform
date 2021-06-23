package app.gui.panel;


import app.Contracts;
import app.util.Resources;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
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
import java.awt.datatransfer.StringSelection;
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

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField companyField;
    private JTextField addressField;
    private JTextArea biographyArea;

    private JButton saveButton;

    private final AtomicBoolean saveButtonClickable;

    private User user;

    private final Thread buttonControlThread;

    private byte[] newImage;

    public ProfileInformationPanel() {

        panel.requestFocus();

        saveButtonClickable = new AtomicBoolean(false);

        newImage = null;

        saveButton.setForeground(Contracts.DEFAULT_WHITE);

        imageLabel.setBorder(new LineBorder(Color.BLACK, 5));
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.setFocusable(true);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
            }
        });

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (saveButtonClickable.get()) {
                    saveButtonClickable.set(false);
                    saveInformationLabel.setText("");
                    saveButton.setForeground(Contracts.DEFAULT_WHITE);
                    saveInformation();
                } else {
                    saveInformationLabel.setText("No changes detected");
                }
            }
        });

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.requestFocusInWindow();
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Choose Image");
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setAcceptAllFileFilterUsed(false);
                jfc.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
                int returnValue = jfc.showOpenDialog(ProfileInformationPanel.this.panel);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    String imageName = selectedFile.getName();
                    String extension = imageName.substring(imageName.lastIndexOf(".") + 1, imageName.length());
                    try {
                        BufferedImage image = Resources.cropAndResizeDefaultSize(ImageIO.read(selectedFile));
                        imageLabel.setIcon(new ImageIcon(image));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
                        ImageIO.write(image, extension, imageOutputStream);
                        newImage = byteArrayOutputStream.toByteArray();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        usernameLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                StringSelection stringSelection = new StringSelection(usernameLabel.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                usernameLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                usernameLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

        emailLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                StringSelection stringSelection = new StringSelection(emailLabel.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                emailLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                emailLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

        buttonControlThread = new Thread(() -> {
            while (!Thread.interrupted()) {

                Information information = user.getInformation();

                if (newImage != null ||
                        (!nameField.getText().equals(information.getName() != null ? information.getName() : "")) ||
                        (!surnameField.getText().equals(information.getSurname() != null ? information.getSurname() : "")) ||
                        (!addressField.getText().equals(information.getAddress() != null ? information.getAddress() : "")) ||
                        (!biographyArea.getText().equals(information.getBiography() != null ? information.getBiography() : "")) ||
                        (!companyField.getText().equals(information.getCompany() != null ? information.getCompany() : ""))
                ) {
                    setSaveButtonActiveness(true);

                } else {
                    setSaveButtonActiveness(false);
                }

            }
        });

        buttonControlThread.setDaemon(true);

    }

    public void loadAndStartPanel(User user) {
        this.user = user;
        setInformationText();
        buttonControlThread.start();
    }

    public void interrupt() {
        buttonControlThread.interrupt();
    }

    private void setSaveButtonActiveness(boolean active) {
        saveButtonClickable.set(active);
        saveButton.setForeground(active ? Color.WHITE : Contracts.DEFAULT_WHITE);
    }

    private void setInformationText() {

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

        Information information = user.getInformation();

        if (information.getImage() != null) {

            ByteArrayInputStream imageStream = new ByteArrayInputStream(information.getImage());
            try {
                ImageIcon imageIcon = new ImageIcon(ImageIO.read(imageStream));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
                loadDefaultImage();
            }

        } else {
            loadDefaultImage();
        }

        nameField.setText(information.getName() != null ? information.getName() : "");
        surnameField.setText(information.getSurname() != null ? information.getSurname() : "");
        addressField.setText(information.getAddress() != null ? information.getAddress() : "");
        companyField.setText(information.getCompany() != null ? information.getCompany() : "");
        biographyArea.setText(information.getBiography() != null ? information.getBiography() : "");

    }

    private void loadDefaultImage() {
        imageLabel.setIcon(Resources.defaultImageIcon);
    }

    private void saveInformation() {
        Information newInformation = generateNewInformation();
        if (!user.getInformation().equals(newInformation)) {
            user.setInformation(newInformation);
            DataHandler.getDataHandler().informationSaveAsync(newInformation, new DataListener<Information>() {
                @Override
                public void onResult(ApiResponse<Information> response) {
                    saveInformationLabel.setText(response.getMessage());
                }
            });
        }

    }

    private Information generateNewInformation() {

        Information newInformation = new Information();

        newInformation.setId(user.getInformation().getId());
        newInformation.setName(nameField.getText());
        newInformation.setSurname(surnameField.getText());
        newInformation.setAddress(addressField.getText());
        newInformation.setCompany(companyField.getText());
        newInformation.setBiography(biographyArea.getText());

        if (newImage != null) {
            newInformation.setImage(newImage);
            newImage = null;
        } else {
            newInformation.setImage(user.getInformation().getImage());
        }

        return newInformation;

    }

    public JPanel getPanel() {
        return panel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(15, 2, new Insets(20, 20, 20, 20), -1, -1));
        nameLabel = new JLabel();
        Font nameLabelFont = this.$$$getFont$$$(null, -1, 19, nameLabel.getFont());
        if (nameLabelFont != null) nameLabel.setFont(nameLabelFont);
        nameLabel.setHorizontalAlignment(0);
        nameLabel.setText("Name");
        panel.add(nameLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        surnameLabel = new JLabel();
        Font surnameLabelFont = this.$$$getFont$$$(null, -1, 19, surnameLabel.getFont());
        if (surnameLabelFont != null) surnameLabel.setFont(surnameLabelFont);
        surnameLabel.setHorizontalAlignment(0);
        surnameLabel.setText("Surname");
        panel.add(surnameLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameField = new JTextField();
        panel.add(nameField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        surnameField = new JTextField();
        surnameField.setText("");
        panel.add(surnameField, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        panel.add(saveButton, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressLabel = new JLabel();
        Font addressLabelFont = this.$$$getFont$$$(null, -1, 19, addressLabel.getFont());
        if (addressLabelFont != null) addressLabel.setFont(addressLabelFont);
        addressLabel.setHorizontalAlignment(0);
        addressLabel.setText("Address");
        panel.add(addressLabel, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressField = new JTextField();
        panel.add(addressField, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        biographyLabel = new JLabel();
        Font biographyLabelFont = this.$$$getFont$$$(null, -1, 19, biographyLabel.getFont());
        if (biographyLabelFont != null) biographyLabel.setFont(biographyLabelFont);
        biographyLabel.setHorizontalAlignment(0);
        biographyLabel.setText("Biography");
        panel.add(biographyLabel, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel.add(spacer2, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel.add(spacer3, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        companyLabel = new JLabel();
        Font companyLabelFont = this.$$$getFont$$$(null, -1, 19, companyLabel.getFont());
        if (companyLabelFont != null) companyLabel.setFont(companyLabelFont);
        companyLabel.setHorizontalAlignment(0);
        companyLabel.setText("Company");
        panel.add(companyLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        companyField = new JTextField();
        companyField.setText("");
        panel.add(companyField, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        biographyArea = new JTextArea();
        biographyArea.setTabSize(4);
        panel.add(biographyArea, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        saveInformationLabel = new JLabel();
        saveInformationLabel.setHorizontalAlignment(0);
        saveInformationLabel.setText("");
        panel.add(saveInformationLabel, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emailLabel = new JLabel();
        Font emailLabelFont = this.$$$getFont$$$(null, -1, 25, emailLabel.getFont());
        if (emailLabelFont != null) emailLabel.setFont(emailLabelFont);
        emailLabel.setText("");
        panel.add(emailLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameLabel = new JLabel();
        Font usernameLabelFont = this.$$$getFont$$$(null, -1, 25, usernameLabel.getFont());
        if (usernameLabelFont != null) usernameLabel.setFont(usernameLabelFont);
        usernameLabel.setText("");
        panel.add(usernameLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(0);
        imageLabel.setHorizontalTextPosition(0);
        imageLabel.setText("");
        panel.add(imageLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel.add(spacer4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
