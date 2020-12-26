package app.gui.panel;

import app.gui.dialog.CreateArticleDialog;
import com.bulenkov.darcula.ui.DarculaButtonUI;
import com.bulenkov.darcula.ui.DarculaInternalFrameUI;
import core.entity.User;
import core.entity.superficial.SuperficialArticle;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Stack;

public class ProfileArticlePanel {

    private JPanel panel;
    private JScrollPane articlePanel;
    private JPanel articleInnerPanel;
    private JButton createButton;

    public ProfileArticlePanel() {

        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                CreateArticleDialog dialog=new CreateArticleDialog();
                dialog.setVisible(true);

            }
        });

    }

    public void loadAndStartPanel(User user){
        List<SuperficialArticle> articleList=user.getOwnArticles();
        ((GridLayout)articleInnerPanel.getLayout()).setRows(articleList.size());
        for(SuperficialArticle article:articleList){
            System.out.println(article);
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
