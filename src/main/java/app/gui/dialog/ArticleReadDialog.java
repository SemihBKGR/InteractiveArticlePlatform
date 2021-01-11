package app.gui.dialog;

import core.entity.Article;

import javax.swing.*;
import java.awt.event.*;

public class ArticleReadDialog extends JDialog {

    private JPanel contentPane;
    private JScrollPane contentScrollPanel;
    private JTextArea contentTextArea;
    private JLabel titleLabel;

    public ArticleReadDialog(Article article) {
        setContentPane(contentPane);
        setModal(true);

        setSize(750,500);
        contentScrollPanel.getVerticalScrollBar().setUnitIncrement(17);

        titleLabel.setText(article.getTitle());
        contentTextArea.setText(article.getContent());

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
    }


}
