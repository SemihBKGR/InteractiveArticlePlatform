package app.gui.panel;

import core.entity.superficial.SuperficialArticle;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DateFormat;
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

        titleLabel.setText("Title : "+article.getTitle());
        updateLabel.setText("Last Update : "+ DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault()).format(new Date(article.getUpdated_at())).toString());
        createDateLabel.setText("Created at : "+DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault()).format(new Date(article.getCreated_at())).toString());
        isReleasedLabel.setText("Status : "+(article.is_released()?"Released":"Writing"));

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
