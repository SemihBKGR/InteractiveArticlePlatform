package app.gui.panel;

import app.util.Paged;
import core.DataHandler;
import core.entity.Article;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;
import core.entity.superficial.SuperficialUser;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;

public class TabArticlePanel {

    private JPanel panel;
    private JLabel titleLabel;
    private JLabel updateLabel;
    private JLabel createLabel;
    private JLabel statusLabel;
    private JButton editButton;
    private JLabel editWarnLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel contributorPanel;
    private JLabel permissionLabel;

    public TabArticlePanel(Article article, Paged paged){

        editButton.setVisible(false);

        titleLabel.setText(article.getTitle());

        String pattern="dd-M-yyyy hh:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
        createLabel.setText("Created at : "+dateFormat.format(new Date(article.getCreated_at())));
        updateLabel.setText("Last update : "+dateFormat.format(new Date(article.getUpdate_at())));
        statusLabel.setText("Status : "+(article.is_private()?"Private":"Public")+(article.is_released()?"Published":"Writing"));

        populateContributors(article,paged);

        DataHandler.getDataHandler().getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                if(response.getData().getOwnArticles().stream().anyMatch(article1 -> article1.getId()==article.getId())
                || response.getData().getContributorArticle().stream().anyMatch(article1 -> article1.getId()==article.getId())){
                    editButton.setVisible(true);
                }
            }
        });

    }

    private void populateContributors(Article article,Paged paged){

        DataHandler dataHandler=DataHandler.getDataHandler();

        ((GridLayout)contributorPanel.getLayout()).setRows((Math.max(article.getContributors().size() + 1, 5)));

        dataHandler.getUserAsync(article.getOwner().getId(), new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                contributorPanel.add(new OneLineUserPanel(response.getData(),paged).getPanel());
                contributorPanel.invalidate();
            }
        });

        for(SuperficialUser superficialUser:article.getContributors()){
            dataHandler.getUserAsync(superficialUser.getId(), new DataListener<User>() {
                @Override
                public void onResult(ApiResponse<User> response) {
                    contributorPanel.add(new OneLineUserPanel(response.getData(),paged).getPanel());
                    contributorPanel.invalidate();
                }
            });
        }

    }


    public JPanel getPanel() {
        return panel;
    }


    private void createUIComponents() {
        contributorPanel=new JPanel(new GridLayout(0,1));
    }
}
