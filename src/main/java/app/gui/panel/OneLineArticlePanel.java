package app.gui.panel;

import core.entity.Article;
import core.entity.superficial.SuperficialArticle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
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


    public OneLineArticlePanel(SuperficialArticle article){

        panel.setBorder(new LineBorder(Color.BLACK,1));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        String pattern="dd-M-yyyy hh:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);

        titleLabel.setText("Title : "+article.getTitle());
        updateLabel.setText("Last Update : "+ dateFormat.format(new Date(article.getUpdated_at())));
        createDateLabel.setText("Created at : "+dateFormat.format(new Date(article.getCreated_at())));
        isReleasedLabel.setText("Status : "+(article.is_private()?"Private":"Public")+"/"+(article.is_released()?"Released":"Writing"));

    }

    public OneLineArticlePanel(Article article){

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
