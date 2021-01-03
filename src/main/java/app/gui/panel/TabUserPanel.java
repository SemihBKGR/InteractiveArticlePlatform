package app.gui.panel;

import app.util.Paged;
import app.util.Resources;
import core.entity.Information;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
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

        nameLabel.setText("Name : "+(information.getName()==null?"":information.getName()));
        surnameLabel.setText("Surname : "+information.getSurname());
        phoneLabel.setText("Phone : "+information.getPhone());
        companyLabel.setText("Company : "+information.getCompany());
        addressLabel.setText("Address : "+information.getAddress());
        biographyLabel.setText("Biography : "+information.getAddress());

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

    }

    private void loadDefaultImage(){
        imageLabel.setIcon(Resources.defaultImageIcon);
    }

    private void populateArticle(User user,Paged paged){

        articleCount=user.getOwnArticles().size()+user.getContributorArticle().size();
        ((GridLayout)articleInnerPanel.getLayout()).setRows(articleCount);

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
