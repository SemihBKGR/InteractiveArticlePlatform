package app.gui.page;

import app.gui.panel.TabArticlePanel;
import app.gui.panel.TabPanel;
import app.gui.panel.TabUserPanel;
import app.util.ClickListener;
import app.util.Paged;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import core.entity.Article;
import core.entity.Comment;
import core.entity.User;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;

public class MenuPage {

    private JPanel panel;
    private JTabbedPane tabbedPanel;

    private ClickListener closeTab;

    public MenuPage() {

    }

    public void loadArticlePanel(Article article, Paged paged) {
        int index = -1;
        if ((index = tabbedPanel.indexOfTab(article.getId() + article.getTitle())) != -1) {
            tabbedPanel.setSelectedIndex(index);
        } else {
            tabbedPanel.addTab(article.getId() + article.getTitle(), new TabArticlePanel(article, paged).getPanel());
            tabbedPanel.setTabComponentAt(tabbedPanel.indexOfTab(article.getId() + article.getTitle()),
                    new TabPanel(article, () -> {
                        tabbedPanel.remove(tabbedPanel.indexOfTab(article.getId() + article.getTitle()));
                    }).getPanel());
            tabbedPanel.setSelectedIndex(tabbedPanel.indexOfTab(article.getId() + article.getTitle()));
        }
    }

    public void loadUserPanel(User user, Paged paged) {
        int index = -1;
        if ((index = tabbedPanel.indexOfTab(user.getUsername())) != -1) {
            tabbedPanel.setSelectedIndex(index);
        } else {
            tabbedPanel.addTab(user.getUsername(), new TabUserPanel(user, paged).getPanel());
            tabbedPanel.setTabComponentAt(tabbedPanel.indexOfTab(user.getUsername()),
                    new TabPanel(user, () -> {
                        tabbedPanel.remove(tabbedPanel.indexOfTab(user.getUsername()));

                    }).getPanel());
            tabbedPanel.setSelectedIndex(tabbedPanel.indexOfTab(user.getUsername()));
        }
    }

    public JPanel getPanel() {
        return panel;
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
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPanel = new JTabbedPane();
        panel.add(tabbedPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}