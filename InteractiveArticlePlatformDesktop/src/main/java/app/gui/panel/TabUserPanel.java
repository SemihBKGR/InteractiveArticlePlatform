package app.gui.panel;

import app.Contracts;
import app.util.Paged;
import app.util.Resources;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import core.DataHandler;
import core.entity.Information;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class TabUserPanel {

    private JPanel panel;
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JPanel profilePanel;
    private JPanel informationPanel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel companyLabel;
    private JLabel addressLabel;
    private JLabel biographyLabel;
    private JPanel articleInnerPanel;
    private JScrollPane articlePanel;
    private JLabel articleCountLabel;
    private JLabel reloadLabel;
    private JLabel reloadInfoLabel;

    private AtomicBoolean reloadClickable;

    public TabUserPanel(User user, Paged paged) {

        $$$setupUI$$$();
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

        setInformation(user.getInformation());
        populateArticle(user, paged);

        reloadClickable = new AtomicBoolean(true);

        articlePanel.getVerticalScrollBar().setUnitIncrement(17);

        reloadLabel.setIcon(new ImageIcon(Resources.getImage("reload.png", 20, 20)));
        reloadLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (reloadClickable.get()) {
                    reloadClickable.set(false);
                    DataHandler.getDataHandler().getUserAsync(user.getId(), false, new DataListener<User>() {
                        @Override
                        public void onStart() {
                            reloadInfoLabel.setText("Reloading ...");
                        }

                        @Override
                        public void onException(Throwable t) {
                            reloadInfoLabel.setText("Something went wrong");
                            reloadClickable.set(true);
                        }

                        @Override
                        public void onResult(ApiResponse<User> response) {
                            setInformation(response.getData().getInformation());
                            articleInnerPanel.removeAll();
                            populateArticle(response.getData(), paged);
                            reloadInfoLabel.setText("User reloaded");
                            reloadClickable.set(true);
                        }
                    });
                }
            }
        });

    }

    private void setInformation(Information information) {

        if (information.getName() != null && !information.getName().isEmpty()) {
            nameLabel.setText("Name:" + information.getName());
        } else {
            nameLabel.setVisible(false);
        }

        if (information.getSurname() != null && !information.getSurname().isEmpty()) {
            surnameLabel.setText("Surname:" + information.getSurname());
        } else {
            surnameLabel.setVisible(false);
        }

        if (information.getCompany() != null && !information.getCompany().isEmpty()) {
            companyLabel.setText("Company:" + information.getCompany());
        } else {
            companyLabel.setVisible(false);
        }

        if (information.getAddress() != null && !information.getAddress().isEmpty()) {
            addressLabel.setText("Address:" + information.getAddress());
        } else {
            addressLabel.setVisible(false);
        }

        if (information.getBiography() != null && !information.getBiography().isEmpty()) {
            biographyLabel.setText("Biography:" + information.getBiography());
        } else {
            biographyLabel.setVisible(false);
        }

        if (information.getImage() != null) {

            ByteArrayInputStream imageStream = new ByteArrayInputStream(information.getImage());
            try {
                ImageIcon imageIcon = new ImageIcon(ImageIO.read(imageStream));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
                imageLabel.setIcon(Resources.defaultImageIcon);
            }

        } else {
            imageLabel.setIcon(Resources.defaultImageIcon);
        }

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

    }

    private void populateArticle(User user, Paged paged) {

        int articleCount = user.getOwnArticles().size() + user.getContributorArticle().size();
        ((GridLayout) articleInnerPanel.getLayout()).setRows(Math.max(5, articleCount));

        for (SuperficialArticle article : user.getOwnArticles()) {
            articleInnerPanel.add(new OneLineArticlePanel(paged, article).getPanel());
        }

        for (SuperficialArticle article : user.getContributorArticle()) {
            articleInnerPanel.add(new OneLineArticlePanel(paged, article).getPanel());
        }

        articleCountLabel.setText("Own Article : " + user.getOwnArticles().size() + " / " + "Contribute Article : " + user.getContributorArticle().size());

    }

    private void createUIComponents() {
        articleInnerPanel = new JPanel(new GridLayout(0, 1));
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        profilePanel = new JPanel();
        profilePanel.setLayout(new GridLayoutManager(8, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(profilePanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usernameLabel = new JLabel();
        Font usernameLabelFont = this.$$$getFont$$$(null, -1, 25, usernameLabel.getFont());
        if (usernameLabelFont != null) usernameLabel.setFont(usernameLabelFont);
        usernameLabel.setText("");
        profilePanel.add(usernameLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        emailLabel = new JLabel();
        Font emailLabelFont = this.$$$getFont$$$(null, -1, 25, emailLabel.getFont());
        if (emailLabelFont != null) emailLabel.setFont(emailLabelFont);
        emailLabel.setText("");
        profilePanel.add(emailLabel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        informationPanel = new JPanel();
        informationPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        profilePanel.add(informationPanel, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nameLabel = new JLabel();
        Font nameLabelFont = this.$$$getFont$$$(null, -1, 20, nameLabel.getFont());
        if (nameLabelFont != null) nameLabel.setFont(nameLabelFont);
        nameLabel.setText("");
        informationPanel.add(nameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        informationPanel.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        surnameLabel = new JLabel();
        Font surnameLabelFont = this.$$$getFont$$$(null, -1, 20, surnameLabel.getFont());
        if (surnameLabelFont != null) surnameLabel.setFont(surnameLabelFont);
        surnameLabel.setText("");
        informationPanel.add(surnameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        companyLabel = new JLabel();
        Font companyLabelFont = this.$$$getFont$$$(null, -1, 20, companyLabel.getFont());
        if (companyLabelFont != null) companyLabel.setFont(companyLabelFont);
        companyLabel.setText("");
        informationPanel.add(companyLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addressLabel = new JLabel();
        Font addressLabelFont = this.$$$getFont$$$(null, -1, 20, addressLabel.getFont());
        if (addressLabelFont != null) addressLabel.setFont(addressLabelFont);
        addressLabel.setText("");
        informationPanel.add(addressLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        biographyLabel = new JLabel();
        Font biographyLabelFont = this.$$$getFont$$$(null, -1, 20, biographyLabel.getFont());
        if (biographyLabelFont != null) biographyLabel.setFont(biographyLabelFont);
        biographyLabel.setText("");
        informationPanel.add(biographyLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        profilePanel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        profilePanel.add(spacer3, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        imageLabel = new JLabel();
        imageLabel.setText("");
        profilePanel.add(imageLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        profilePanel.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        profilePanel.add(spacer5, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        profilePanel.add(spacer6, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        profilePanel.add(spacer7, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        articlePanel = new JScrollPane();
        panel.add(articlePanel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        articlePanel.setViewportView(articleInnerPanel);
        articleCountLabel = new JLabel();
        Font articleCountLabelFont = this.$$$getFont$$$(null, -1, 20, articleCountLabel.getFont());
        if (articleCountLabelFont != null) articleCountLabel.setFont(articleCountLabelFont);
        articleCountLabel.setHorizontalAlignment(0);
        articleCountLabel.setText("");
        panel.add(articleCountLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadLabel = new JLabel();
        reloadLabel.setText("");
        panel.add(reloadLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadInfoLabel = new JLabel();
        reloadInfoLabel.setText("");
        panel.add(reloadInfoLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
