package app.gui.dialog;


import app.util.Confirmation;
import app.util.Resources;
import core.DataHandler;
import core.entity.Article;
import core.entity.User;
import core.entity.dto.ArticleCreateDto;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.Entities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class CreateArticleDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField titleField;
    private JCheckBox privateCheckBox;
    private JLabel titleLabel;
    private JLabel warnMessages;

    private AtomicBoolean okButtonClickable;

    private Article article;

    public CreateArticleDialog() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setMinimumSize(new Dimension(500,250));
        okButtonClickable=new AtomicBoolean(true);
        setIconImage(Resources.getImageIcon("article.png").getImage());
        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(okButtonClickable.get()){
                    okButtonClickable.set(false);
                    warnMessages.setText("");
                    String title=titleField.getText();
                    Confirmation.ConfirmationMessage message=Confirmation.articleTitleConfirmation(title);
                    if(message.isConfirmed()){
                        ArticleCreateDto articleCreateDto=new ArticleCreateDto();
                        articleCreateDto.setTitle(titleField.getText());
                        articleCreateDto.set_private(privateCheckBox.isSelected());
                        DataHandler.getDataHandler().createArticleAsync(articleCreateDto, new DataListener<Article>() {
                            @Override
                            public void onException(Throwable t) {
                                okButtonClickable.set(true);
                                warnMessages.setText("Exception occurred");
                            }
                            @Override
                            public void onResult(ApiResponse<Article> response) {
                                article=response.getData();
                                warnMessages.setText(response.getMessage());
                                dispose();
                            }
                        });

                    }else{
                        warnMessages.setText(message.getMessages());
                        okButtonClickable.set(true);
                    }
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void onCancel() {
        dispose();
    }

    public Article getArticle() {
        return article;
    }

}
