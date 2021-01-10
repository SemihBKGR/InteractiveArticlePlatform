package app.gui.dialog;

import core.DataHandler;
import core.entity.Article;
import core.entity.dto.ArticleSaveDto;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ArticleEditDialog extends JDialog {

    private JPanel contentPane;
    private JButton saveButton;
    private JPanel toolPanel;
    private JLabel titleLabel;
    private JTextArea contentTextArea;
    private JLabel warnLabel;

    private volatile String dialogMessage;
    private volatile Article savedArticle;

    private AtomicBoolean buttonsClickable;

    public ArticleEditDialog(Article article) {

        setContentPane(contentPane);
        setModal(true);

        setSize(500,700);
        buttonsClickable=new AtomicBoolean(true);

        contentTextArea.setText(article.getContent());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(buttonsClickable.get()){
                    showSaveDialog(article,true);
                }
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(buttonsClickable.get()){
                    dispose();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        titleLabel.setText(article.getTitle());
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(buttonsClickable.get()){
                    showSaveDialog(article,false);
                }
            }
        });
    }

    private void showSaveDialog(Article article,boolean disposeAfter){
        buttonsClickable.set(false);
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog
                (null, "Are you sure to save changes?","Save",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            ArticleSaveDto articleSaveDto=new ArticleSaveDto();
            articleSaveDto.setId(article.getId());
            articleSaveDto.setContent(contentTextArea.getText());
            DataHandler.getDataHandler().saveArticleAsync(articleSaveDto, new DataListener<Article>() {
                @Override
                public void onException(Throwable t) {
                    warnLabel.setText("Something went wrong");
                    buttonsClickable.set(true);
                }
                @Override
                public void onResult(ApiResponse<Article> response) {
                    if(response.isConfirmed()){
                        dialogMessage=response.getMessage();
                        savedArticle=response.getData();
                        warnLabel.setText(response.getMessage());
                        if(disposeAfter){
                            dispose();
                        }
                    }else{
                        warnLabel.setText(response.getMessage());
                    }
                    buttonsClickable.set(true);
                }
            });
        }else{
            dispose();
        }
    }


    public String getDialogMessage() {
        return dialogMessage;
    }

    public Article getSavedArticle() {
        return savedArticle;
    }

}
