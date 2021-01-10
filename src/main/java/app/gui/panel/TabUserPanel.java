package app.gui.panel;

import app.Contracts;
import app.util.Paged;
import app.util.Resources;
import core.entity.Information;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TabUserPanel {

    private JPanel panel;
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JPanel profilePanel;
    private JPanel informationPanel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel phoneLabel;
    private JLabel companyLabel;
    private JLabel addressLabel;
    private JLabel biographyLabel;
    private JPanel articleInnerPanel;
    private JScrollPane articlePanel;

    private int articleCount;

    public TabUserPanel(User user, Paged paged){

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

        setInformation(user.getInformation());
        populateArticle(user,paged);

        articlePanel.getVerticalScrollBar().setUnitIncrement(17);

    }

    private void setInformation(Information information){

        if(information.getName()!=null && !information.getName().isEmpty()){
            nameLabel.setText("Name:"+information.getName());
        }else{
            nameLabel.setVisible(false);
        }

        if(information.getSurname()!=null && !information.getSurname().isEmpty()){
            surnameLabel.setText("Surname:"+information.getSurname());
        }else{
            surnameLabel.setVisible(false);
        }

        if(information.getPhone()!=null && !information.getPhone().isEmpty()){
            phoneLabel.setText("Phone:"+information.getPhone());
        }else{
            phoneLabel.setVisible(false);
        }

        if(information.getCompany()!=null && !information.getCompany().isEmpty()){
            companyLabel.setText("Company:"+information.getCompany());
        }else{
            companyLabel.setVisible(false);
        }

        if(information.getAddress()!=null && !information.getAddress().isEmpty()){
            addressLabel.setText("Address:"+information.getAddress());
        }else{
            addressLabel.setVisible(false);
        }

        if(information.getBiography()!=null && !information.getBiography().isEmpty()){
            biographyLabel.setText("Biography:"+information.getBiography());
        }else{
            biographyLabel.setVisible(false);
        }

        if(information.getImage()!=null){

            ByteArrayInputStream imageStream=new ByteArrayInputStream(information.getImage());
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

        usernameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                StringSelection stringSelection=new StringSelection(usernameLabel.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection,null);
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
                StringSelection stringSelection=new StringSelection(emailLabel.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection,null);
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

    private void populateArticle(User user,Paged paged){

        articleCount=user.getOwnArticles().size()+user.getContributorArticle().size();
        ((GridLayout)articleInnerPanel.getLayout()).setRows(Math.max(5,articleCount));

        for(SuperficialArticle article:user.getOwnArticles()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

        for(SuperficialArticle article:user.getContributorArticle()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

    }

    private void createUIComponents() {
        articleInnerPanel=new JPanel(new GridLayout(0,1));
    }


    public JPanel getPanel() {
        return panel;
    }

}
