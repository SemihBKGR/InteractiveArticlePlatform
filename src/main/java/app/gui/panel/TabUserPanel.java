package app.gui.panel;

import app.Contracts;
import app.util.Paged;
import app.util.Resources;
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

    public TabUserPanel(User user, Paged paged){

        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());

        setInformation(user.getInformation());
        populateArticle(user,paged);

        reloadClickable=new AtomicBoolean(true);

        articlePanel.getVerticalScrollBar().setUnitIncrement(17);

        reloadLabel.setIcon(new ImageIcon(Resources.getImage("reload.png",20,20)));
        reloadLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(reloadClickable.get()){
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
                            populateArticle(response.getData(),paged);
                            reloadInfoLabel.setText("User reloaded");
                            reloadClickable.set(true);
                        }
                    });
                }
            }
        });

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

        int articleCount=user.getOwnArticles().size()+user.getContributorArticle().size();
        ((GridLayout)articleInnerPanel.getLayout()).setRows(Math.max(5,articleCount));

        for(SuperficialArticle article:user.getOwnArticles()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

        for(SuperficialArticle article:user.getContributorArticle()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

        articleCountLabel.setText("Own Article : "+user.getOwnArticles().size()+" / "+"Contribute Article : "+user.getContributorArticle().size());

    }

    private void createUIComponents() {
        articleInnerPanel=new JPanel(new GridLayout(0,1));
    }

    public JPanel getPanel() {
        return panel;
    }

}
