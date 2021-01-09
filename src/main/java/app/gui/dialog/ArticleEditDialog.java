package app.gui.dialog;

import core.DataHandler;
import core.entity.Article;

import javax.swing.*;
import java.awt.event.*;

public class ArticleEditDialog extends JDialog {

    private JPanel contentPane;
    private JButton saveButton;
    private JPanel toolPanel;
    private JLabel titleLabel;
    private JFormattedTextField contentTextArea;

    public ArticleEditDialog(Article article) {

        setContentPane(contentPane);
        setModal(true);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        titleLabel.setText(article.getTitle());

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog
                        (null, "Are you sure to save changes?","Save",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){

                }else{

                }
            }
        });


    }


}
