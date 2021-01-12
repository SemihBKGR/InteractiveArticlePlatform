package app.gui.panel;

import app.Contracts;
import app.gui.dialog.CreateArticleDialog;
import app.util.Paged;
import app.util.Resources;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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

        this.paged = paged;

        reloadClickable = new AtomicBoolean(true);
        ownCount = new AtomicInteger(0);
        contributeCount = new AtomicInteger(0);

        $$$setupUI$$$();
        articlePanel.getVerticalScrollBar().setUnitIncrement(17);

        reloadLabel.setIcon(new ImageIcon(Resources.getImage("reload.png", 20, 20)));

        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CreateArticleDialog dialog = new CreateArticleDialog();
                dialog.setLocation(new Point((int) MouseInfo.getPointerInfo().getLocation().getX() - dialog.getWidth() / 2,
                        (int) MouseInfo.getPointerInfo().getLocation().getY() - dialog.getHeight() / 2));
                dialog.setVisible(true);
                if (dialog.getArticle() != null) {
                    SuperficialArticle createdArticle = Entities.articleToSuperficialArticle(dialog.getArticle());
                    user.getContributorArticle().add(createdArticle);
                    ((GridLayout) articleInnerPanel.getLayout()).setRows(Math.max(5, ownCount.incrementAndGet()));
                    articleInnerPanel.add(new OneLineArticlePanel(paged, createdArticle).getPanel());
                    infoLabel.setText("New article '" + createdArticle.getTitle() + "' created");
                    articleCountLabel.setText("Own Article : " + ownCount.get() + " / " + "Contribute Article : " + contributeCount.get());
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

                if (reloadClickable.get()) {
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

    public void loadAndStartPanel(User user) {

        this.user = user;

        ownCount.set(user.getOwnArticles().size());
        contributeCount.set(user.getContributorArticle().size());
        int articleCount = ownCount.get() + contributeCount.get();

        infoLabel.setText("Article count : " + articleCount);

        ((GridLayout) articleInnerPanel.getLayout()).setRows(Math.max(5, articleCount));

        for (SuperficialArticle article : user.getOwnArticles()) {
            articleInnerPanel.add(new OneLineArticlePanel(paged, article).getPanel());
        }

        for (SuperficialArticle article : user.getContributorArticle()) {
            articleInnerPanel.add(new OneLineArticlePanel(paged, article).getPanel());
        }

        articleCountLabel.setText("Own Article : " + ownCount.get() + " / " + "Contribute Article : " + contributeCount.get());

    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {

        articleInnerPanel = new JPanel();
        articleInnerPanel.setLayout(new GridLayout(0, 1));


    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 2, new Insets(20, 20, 20, 20), -1, -1));
        articlePanel = new JScrollPane();
        articlePanel.setAlignmentX(1.0f);
        articlePanel.setAlignmentY(1.0f);
        panel.add(articlePanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        articlePanel.setViewportView(articleInnerPanel);
        createButton = new JButton();
        createButton.setText("Create");
        panel.add(createButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        infoLabel = new JLabel();
        infoLabel.setHorizontalAlignment(0);
        infoLabel.setText("");
        panel.add(infoLabel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadLabel = new JLabel();
        reloadLabel.setHorizontalTextPosition(2);
        reloadLabel.setText("");
        panel.add(reloadLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        articleCountLabel = new JLabel();
        Font articleCountLabelFont = this.$$$getFont$$$(null, -1, 20, articleCountLabel.getFont());
        if (articleCountLabelFont != null) articleCountLabel.setFont(articleCountLabelFont);
        articleCountLabel.setHorizontalAlignment(0);
        articleCountLabel.setText("");
        panel.add(articleCountLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return panel;
    }
}
