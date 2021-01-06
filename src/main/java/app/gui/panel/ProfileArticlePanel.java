package app.gui.panel;

import app.gui.dialog.CreateArticleDialog;
import app.util.Paged;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;
import core.util.Entities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProfileArticlePanel {

    private JPanel panel;
    private JScrollPane articlePanel;
    private JPanel articleInnerPanel;
    private JButton createButton;

    private User user;
    private int articleCount;

    Paged paged;

    public ProfileArticlePanel(Paged paged) {

        this.paged=paged;

        articlePanel.getVerticalScrollBar().setUnitIncrement(17);

        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateArticleDialog dialog=new CreateArticleDialog();
                dialog.setLocation(new Point((int)MouseInfo.getPointerInfo().getLocation().getX()-dialog.getWidth()/2,
                        (int)MouseInfo.getPointerInfo().getLocation().getY()-dialog.getHeight()/2));
                dialog.setVisible(true);
                if(dialog.getArticle()!=null){
                    SuperficialArticle createdArticle=Entities.articleToSuperficialArticle(dialog.getArticle());
                    articleCount++;
                    user.getContributorArticle().add(createdArticle);
                    ((GridLayout)articleInnerPanel.getLayout()).setRows(articleCount);
                    articleInnerPanel.add(new OneLineArticlePanel(paged,createdArticle).getPanel());

                }
            }
        });

    }

    public void loadAndStartPanel(User user){

        this.user=user;
        articleCount=user.getOwnArticles().size()+user.getContributorArticle().size();

        ((GridLayout)articleInnerPanel.getLayout()).setRows(articleCount);

        for(SuperficialArticle article:user.getOwnArticles()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

        for(SuperficialArticle article:user.getContributorArticle()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
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
