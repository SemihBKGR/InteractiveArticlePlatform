package app.gui.panel;

import app.gui.dialog.CreateArticleDialog;
import com.bulenkov.darcula.ui.DarculaButtonUI;
import com.bulenkov.darcula.ui.DarculaInternalFrameUI;
import core.entity.Article;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;
import core.entity.superficial.SuperficialUser;
import core.util.Entities;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProfileArticlePanel {

    private JPanel panel;
    private JScrollPane articlePanel;
    private JPanel articleInnerPanel;
    private JButton createButton;

    private User user;
    private int articleCount;


    public ProfileArticlePanel() {

        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateArticleDialog dialog=new CreateArticleDialog();
                dialog.setVisible(true);
                if(dialog.getArticle()!=null){
                    SuperficialArticle createdArticle=Entities.articleToSuperficialArticle(dialog.getArticle());
                    articleCount++;
                    user.getContributorArticle().add(createdArticle);
                    ((GridLayout)articleInnerPanel.getLayout()).setRows(articleCount);
                    articleInnerPanel.add(new OneLineArticlePanel(createdArticle).getPanel());
                }
            }
        });

    }

    public void loadAndStartPanel(User user){

        this.user=user;
        articleCount=user.getOwnArticles().size()+user.getContributorArticle().size();

        ((GridLayout)articleInnerPanel.getLayout()).setRows(articleCount);

        for(SuperficialArticle article:user.getOwnArticles()){
            articleInnerPanel.add(new OneLineArticlePanel(article).getPanel());
        }

        for(SuperficialArticle article:user.getContributorArticle()){
            articleInnerPanel.add(new OneLineArticlePanel(article).getPanel());
        }

    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        articleInnerPanel=new JPanel();
        articleInnerPanel.setLayout(new GridLayout(0,1));
    }

}
