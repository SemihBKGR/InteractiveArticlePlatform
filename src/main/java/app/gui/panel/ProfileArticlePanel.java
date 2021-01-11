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
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileArticlePanel {

    private JPanel panel;
    private JScrollPane articlePanel;
    private JPanel articleInnerPanel;
    private JButton createButton;
    private JLabel infoLabel;
    private JLabel reloadLabel;
    private JLabel articleCountLabel;

    private User user;

    Paged paged;

    private AtomicBoolean reloadClickable;

    private AtomicInteger ownCount;
    private AtomicInteger contributeCount;

    public ProfileArticlePanel(Paged paged) {

        this.paged=paged;

        reloadClickable=new AtomicBoolean(true);
        ownCount=new AtomicInteger(0);
        contributeCount=new AtomicInteger(0);

        articlePanel.getVerticalScrollBar().setUnitIncrement(17);

        reloadLabel.setIcon(new ImageIcon(Resources.getImage("reload.png",20,20)));

        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateArticleDialog dialog=new CreateArticleDialog();
                dialog.setLocation(new Point((int)MouseInfo.getPointerInfo().getLocation().getX()-dialog.getWidth()/2,
                        (int)MouseInfo.getPointerInfo().getLocation().getY()-dialog.getHeight()/2));
                dialog.setVisible(true);
                if(dialog.getArticle()!=null){
                    SuperficialArticle createdArticle=Entities.articleToSuperficialArticle(dialog.getArticle());
                    user.getContributorArticle().add(createdArticle);
                    ((GridLayout)articleInnerPanel.getLayout()).setRows(Math.max(5,ownCount.incrementAndGet()));
                    articleInnerPanel.add(new OneLineArticlePanel(paged,createdArticle).getPanel());
                    infoLabel.setText("New article '"+createdArticle.getTitle()+"' created");
                    articleCountLabel.setText("Own Article : "+ownCount.get()+" / "+"Contribute Article : "+contributeCount.get());
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
                        public void onStart() {
                            infoLabel.setText("Reloading ...");
                        }

                        @Override
                        public void onException(Throwable t) {
                            infoLabel.setText("Articles cannot be reloaded, something went wrong");
                            reloadClickable.set(true);
                        }
                        @Override
                        public void onResult(ApiResponse<User> apiResponse) {
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

        ownCount.set(user.getOwnArticles().size());
        contributeCount.set(user.getContributorArticle().size());
        int articleCount=ownCount.get()+contributeCount.get();

        infoLabel.setText("Article count : "+articleCount);

        ((GridLayout)articleInnerPanel.getLayout()).setRows(Math.max(5,articleCount));

        for(SuperficialArticle article:user.getOwnArticles()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

        for(SuperficialArticle article:user.getContributorArticle()){
            articleInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
        }

        articleCountLabel.setText("Own Article : "+ownCount.get()+" / "+"Contribute Article : "+contributeCount.get());

    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {

        articleInnerPanel=new JPanel();
        articleInnerPanel.setLayout(new GridLayout(0,1));



    }

}
