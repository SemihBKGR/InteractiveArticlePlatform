package app.gui.dialog;

import app.gui.panel.OneLineRemoveContributorPanel;
import app.util.Paged;
import app.util.Resources;
import core.entity.Article;
import core.entity.superficial.SuperficialUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RemoveContributorDialog extends JDialog {

    private JPanel contentPane;
    private JPanel panel;

    private Article article;

    public RemoveContributorDialog(Article article, Paged paged) {

        setContentPane(contentPane);
        setModal(true);

        this.article=article;

        setSize(500,700);
        setIconImage(Resources.getImageIcon("article.png").getImage());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        populateContributors(article,paged);


    }

    private void populateContributors(Article article,Paged paged){
        int contributorCount=article.getContributors().size();
        ((GridLayout)panel.getLayout()).setRows(Math.max(5,contributorCount));
        for(SuperficialUser user:article.getContributors()){
            panel.add(new OneLineRemoveContributorPanel(user,article.getId(),paged).getPanel());
        }
    }

    private void createUIComponents() {
        panel=new JPanel(new GridLayout(0,1));
    }

}
