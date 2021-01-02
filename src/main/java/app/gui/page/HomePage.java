package app.gui.page;

import app.gui.frame.AppFrame;
import app.gui.panel.TabArticlePanel;
import core.entity.Article;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePage {

    private JPanel panel;
    private JTabbedPane tabbedPanel;
    private JButton searchButton;


    public HomePage(AppFrame appFrame){

       /*searchButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {

               appFrame.changePage(AppFrame.Page.search);

           }
       });*/



    }

    public void loadArticlePanel(Article article){
        tabbedPanel.addTab(article.getTitle(),new TabArticlePanel(article).getPanel());
    }

    public JPanel getPanel() {
        return panel;
    }

}
