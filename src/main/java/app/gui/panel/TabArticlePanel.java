package app.gui.panel;

import core.entity.Article;

import javax.swing.*;

public class TabArticlePanel {

    private JPanel panel;
    private JLabel titleLabel;

    public TabArticlePanel(Article article){

        titleLabel.setText(article.getTitle());

    }

    public JPanel getPanel() {
        return panel;
    }
}
