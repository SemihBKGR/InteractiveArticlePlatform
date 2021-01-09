package app.gui.panel;

import app.Contracts;
import app.util.Paged;
import app.util.TypeConverts;
import core.DataHandler;
import core.entity.Article;
import core.entity.superficial.SuperficialArticle;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneLineArticlePanel {

    private JPanel panel;
    private JLabel titleLabel;
    private JLabel updateLabel;
    private JLabel createDateLabel;
    private JLabel statusLabel;

    private Article article;
    private SuperficialArticle superficialArticle;

    private OneLineArticlePanel(Paged paged){

        panel.setBorder(new LineBorder(Contracts.DEFAULT_LIGHT_GRAY,1));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(article!=null){
                    paged.changePage(ButtonPanel.ActiveButton.menu.name(),article);
                }else{
                    DataHandler.getDataHandler().getArticleAsync(superficialArticle.getId(), new DataListener<Article>() {
                        @Override
                        public void onResult(ApiResponse<Article> response) {
                            paged.changePage(ButtonPanel.ActiveButton.menu.name(),response.getData());
                        }
                    });
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new LineBorder(Contracts.DEFAULT_BLUE,3));
                titleLabel.setForeground(Color.WHITE);
                statusLabel.setForeground(Color.WHITE);
                createDateLabel.setForeground(Color.WHITE);
                updateLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new LineBorder(Contracts.DEFAULT_LIGHT_GRAY,1));
                titleLabel.setForeground(Contracts.DEFAULT_WHITE);
                statusLabel.setForeground(Contracts.DEFAULT_WHITE);
                createDateLabel.setForeground(Contracts.DEFAULT_WHITE);
                updateLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

    }

    public OneLineArticlePanel(Paged paged,SuperficialArticle article){

        this(paged);
        this.superficialArticle=article;

        titleLabel.setText("Title : "+article.getTitle());
        statusLabel.setText("Status : "+(article.is_private()?"Private":"Public")+" / "+(article.is_released()?"Released":"Writing"));
        createDateLabel.setText("Created at : "+TypeConverts.getTimeString(article.getCreated_at()));
        updateLabel.setText("Last Update : "+ TypeConverts.getTimeString(article.getUpdated_at()));

    }

    public OneLineArticlePanel(Paged paged,Article article){

        this(paged);
        this.article=article;

        titleLabel.setText("Title : "+article.getTitle());
        statusLabel.setText("Status : "+(article.is_private()?"Private":"Public")+" / "+(article.is_released()?"Released":"Writing"));
        createDateLabel.setText("Created at : "+TypeConverts.getTimeString(article.getCreated_at()));
        updateLabel.setText("Last Update : "+ TypeConverts.getTimeString(article.getUpdate_at()));


    }

    public JPanel getPanel() {
        return panel;
    }


}
