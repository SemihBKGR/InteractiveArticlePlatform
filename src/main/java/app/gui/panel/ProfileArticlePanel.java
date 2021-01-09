package app.gui.panel;

import app.Contracts;
import app.gui.dialog.CreateArticleDialog;
import app.util.Paged;
import app.util.Resources;
import core.DataHandler;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.Entities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProfileArticlePanel {

    private JPanel panel;
    private JScrollPane articlePanel;
    private JPanel articleInnerPanel;
    private JButton createButton;
    private JLabel reloadLabel;
    private JLabel infoLabel;

    private User user;
    private int articleCount;

    Paged paged;

    private AtomicBoolean reloadClickable;

    public ProfileArticlePanel(Paged paged) {

        this.paged=paged;

        reloadClickable=new AtomicBoolean(true);

        articlePanel.getVerticalScrollBar().setUnitIncrement(17);
        reloadLabel.setIcon(Resources.getImageIcon("reload.png"));

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
                    infoLabel.setText("New article '"+createdArticle.getTitle()+"' created");
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                createButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                createButton.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

        reloadLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if(reloadClickable.get()){
                    reloadClickable.set(false);
                    DataHandler.getDataHandler().getMeAsync(new DataListener<User>() {
                        @Override
                        public void onException(Throwable t) {
                            infoLabel.setText("Articles cannot be reloaded, something went wrong");
                            reloadClickable.set(true);
                        }
                        @Override
                        public void onResult(ApiResponse<User> apiResponse) {
                            infoLabel.setText("Articles reloaded");
                            articleInnerPanel.removeAll();
                            loadAndStartPanel(apiResponse.getData());
                            reloadClickable.set(true);
                        }
                    });
                }
            }
        });

    }

    public void loadAndStartPanel(User user){

        this.user=user;
        articleCount=user.getOwnArticles().size()+user.getContributorArticle().size();

        ((GridLayout)articleInnerPanel.getLayout()).setRows(Math.max(5,articleCount));

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
