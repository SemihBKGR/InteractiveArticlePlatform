package app.gui.dialog;

import app.util.TypeConverts;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import core.DataHandler;
import core.entity.Article;
import core.entity.dto.ArticleSaveDto;
import core.util.ApiResponse;
import core.util.DataListener;
import org.springframework.expression.TypeComparator;

import javax.swing.*;
import java.awt.*;
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

        setSize(750, 500);
        saveButtonClickable = new AtomicBoolean(true);

        contentScrollPanel.getVerticalScrollBar().setUnitIncrement(17);

        contentTextArea.setText(article.getContent());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (saveButtonClickable.get()) {
                    showExitDialog();
                }
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveButtonClickable.get()) {
                    showExitDialog();
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        titleLabel.setText(article.getTitle());
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (saveButtonClickable.get()) {
                    showSaveDialog(article);
                }
            }
        });
    }


    private void showExitDialog() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog
                (null, "Close dialog", "Close", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void showSaveDialog(Article article) {
        saveButtonClickable.set(false);
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog
                (null, "Save changes", "Save", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
            ArticleSaveDto articleSaveDto = new ArticleSaveDto();
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
                    if (response.isConfirmed()) {
                        dialogMessage = response.getMessage();
                        savedArticle = response.getData();
                        warnLabel.setText(response.getMessage());
                        System.out.println(TypeConverts.getTimeString(response.getData().getUpdated_at()));
                    } else {
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        panel1.add(saveButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(toolPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titleLabel = new JLabel();
        Font titleLabelFont = this.$$$getFont$$$(null, -1, 25, titleLabel.getFont());
        if (titleLabelFont != null) titleLabel.setFont(titleLabelFont);
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setText("");
        panel1.add(titleLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentScrollPanel = new JScrollPane();
        panel1.add(contentScrollPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contentTextArea = new JTextArea();
        Font contentTextAreaFont = this.$$$getFont$$$(null, -1, 15, contentTextArea.getFont());
        if (contentTextAreaFont != null) contentTextArea.setFont(contentTextAreaFont);
        contentScrollPanel.setViewportView(contentTextArea);
        warnLabel = new JLabel();
        warnLabel.setHorizontalAlignment(0);
        warnLabel.setText("");
        panel1.add(warnLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
