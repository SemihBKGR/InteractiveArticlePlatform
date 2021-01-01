package app.gui.panel;

import app.gui.frame.AppFrame;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OneLineArticlePanel {
    private JPanel panel;
    private JLabel titleLabel;
    private JLabel updateLabel;
    private JLabel createDateLabel;
    private JLabel isReleasedLabel;

    private Article article;
    private SuperficialArticle superficialArticle;

    private OneLineArticlePanel(AppFrame appFrame){

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(article!=null){
                    appFrame.changePage(AppFrame.Page.home,article);
                }else{
                    DataHandler.getDataHandler().getArticleAsync(superficialArticle.getId(), new DataListener<Article>() {
                        @Override
                        public void onResult(ApiResponse<Article> response) {
                            appFrame.changePage(AppFrame.Page.home,response.getData());
                        }
                    });
                }
            }
        });

    }

    public OneLineArticlePanel(AppFrame appFrame,SuperficialArticle article){

        this(appFrame);
        this.superficialArticle=article;

        panel.setBorder(new LineBorder(Color.BLACK,1));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        String pattern="dd-M-yyyy hh:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);

        titleLabel.setText("Title : "+article.getTitle());
        updateLabel.setText("Last Update : "+ dateFormat.format(new Date(article.getUpdated_at())));
        createDateLabel.setText("Created at : "+dateFormat.format(new Date(article.getCreated_at())));
        isReleasedLabel.setText("Status : "+(article.is_private()?"Private":"Public")+"/"+(article.is_released()?"Released":"Writing"));

    }

    public OneLineArticlePanel(AppFrame appFrame,Article article){

        this(appFrame);
        this.article=article;

        panel.setBorder(new LineBorder(Color.BLACK,1));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        String pattern="dd-M-yyyy hh:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);

        titleLabel.setText("Title : "+article.getTitle());
        updateLabel.setText("Last Update : "+ dateFormat.format(new Date(article.getUpdate_at())));
        createDateLabel.setText("Created at : "+dateFormat.format(new Date(article.getCreated_at())));
        isReleasedLabel.setText("Status : "+(article.is_private()?"Private":"Public")+"/"+(article.is_released()?"Released":"Writing"));

    }

    public JPanel getPanel() {
        return panel;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public JLabel getUpdateLabel() {
        return updateLabel;
    }

    public JLabel getCreateDateLabel() {
        return createDateLabel;
    }

    public JLabel getIsReleasedLabel() {
        return isReleasedLabel;
    }

}
