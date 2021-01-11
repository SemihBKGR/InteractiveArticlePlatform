package app.gui.dialog;

import app.util.TypeConverts;
import core.DataHandler;
import core.entity.Article;
import core.entity.dto.ArticleSaveDto;
import core.util.ApiResponse;
import core.util.DataListener;
import org.springframework.expression.TypeComparator;

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
    private JScrollPane contentScrollPanel;

    private volatile String dialogMessage;
    private volatile Article savedArticle;

    private AtomicBoolean saveButtonClickable;

    public ArticleEditDialog(Article article) {

        setContentPane(contentPane);
        setModal(true);

        setSize(750,500);
        saveButtonClickable =new AtomicBoolean(true);

        contentScrollPanel.getVerticalScrollBar().setUnitIncrement(17);

        contentTextArea.setText(article.getContent());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(saveButtonClickable.get()){
                    showExitDialog();
                }
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(saveButtonClickable.get()){
                    showExitDialog();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        titleLabel.setText(article.getTitle());
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(saveButtonClickable.get()){
                    showSaveDialog(article);
                }
            }
        });
    }


    private void showExitDialog(){
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog
                (null, "Close dialog","Close",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            dispose();
        }
    }

    private void showSaveDialog(Article article){
        saveButtonClickable.set(false);
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog
                (null, "Save changes","Save",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            ArticleSaveDto articleSaveDto=new ArticleSaveDto();
            articleSaveDto.setId(article.getId());
            articleSaveDto.setContent(contentTextArea.getText());
            DataHandler.getDataHandler().saveArticleAsync(articleSaveDto, new DataListener<Article>() {
                @Override
                public void onException(Throwable t) {
                    warnLabel.setText("Something went wrong");
                    saveButtonClickable.set(true);
                }
                @Override
                public void onResult(ApiResponse<Article> response) {
                    if(response.isConfirmed()){
                        dialogMessage=response.getMessage();
                        savedArticle=response.getData();
                        warnLabel.setText(response.getMessage());
                        System.out.println(TypeConverts.getTimeString(response.getData().getUpdated_at()));
                    }else{
                        warnLabel.setText(response.getMessage());
                    }
                    saveButtonClickable.set(true);
                }
            });
        }
    }


    public String getDialogMessage() {
        return dialogMessage;
    }

    public Article getSavedArticle() {
        return savedArticle;
    }

}
