package app.gui.page;

import app.gui.frame.AppFrame;
import app.gui.panel.TabArticlePanel;
import app.gui.panel.TabUserPanel;
import app.util.Paged;
import core.entity.Article;
import core.entity.User;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePage {

    private JPanel panel;
    private JTabbedPane tabbedPanel;


    public HomePage(){




    }

    public void loadArticlePanel(Article article,Paged paged){
        tabbedPanel.addTab(article.getTitle(),new TabArticlePanel(article,paged).getPanel());
    }

    public void loadUserPanel(User user, Paged paged){
        tabbedPanel.addTab(user.getUsername(),new TabUserPanel(user,paged).getPanel());
    }

    public JPanel getPanel() {
        return panel;
    }

}
