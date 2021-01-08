package app.gui.page;

import app.gui.panel.TabArticlePanel;
import app.gui.panel.TabPanel;
import app.gui.panel.TabUserPanel;
import app.util.ClickListener;
import app.util.Paged;
import core.entity.Article;
import core.entity.Comment;
import core.entity.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPage {

    private JPanel panel;
    private JTabbedPane tabbedPanel;

    private ClickListener closeTab;

    public MenuPage(){

    }

    public void loadArticlePanel(Article article,Paged paged){
        int index=-1;
        if((index=tabbedPanel.indexOfTab(article.getId()+article.getTitle()))!=-1){
            tabbedPanel.setSelectedIndex(index);
        }else{
            tabbedPanel.addTab(article.getId()+article.getTitle(),new TabArticlePanel(article,paged).getPanel());
            tabbedPanel.setTabComponentAt(tabbedPanel.indexOfTab(article.getId()+article.getTitle()),
                    new TabPanel(article, () -> {
                        tabbedPanel.remove(tabbedPanel.indexOfTab(article.getId() + article.getTitle()));
                    }).getPanel());
            tabbedPanel.setSelectedIndex(tabbedPanel.indexOfTab(article.getId()+article.getTitle()));
        }

    }

    public void loadUserPanel(User user, Paged paged){
        int index=-1;
        if((index=tabbedPanel.indexOfTab(user.getUsername()))!=-1){
            tabbedPanel.setSelectedIndex(index);
        }else{
            tabbedPanel.addTab(user.getUsername(),new TabUserPanel(user,paged).getPanel());
            tabbedPanel.setTabComponentAt(tabbedPanel.indexOfTab(user.getUsername()),
                    new TabPanel(user, () -> {
                        tabbedPanel.remove(tabbedPanel.indexOfTab(user.getUsername()));

                    }).getPanel());
            tabbedPanel.setSelectedIndex(tabbedPanel.indexOfTab(user.getUsername()));
        }

    }

    public JPanel getPanel() {
        return panel;
    }

}
