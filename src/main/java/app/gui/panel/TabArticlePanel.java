package app.gui.panel;

import app.gui.dialog.ArticleEditDialog;
import app.gui.dialog.ArticleReadDialog;
import app.gui.dialog.AddContributorDialog;
import app.gui.dialog.RemoveContributorDialog;
import app.util.Paged;
import app.util.Resources;
import app.util.TypeConverts;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import core.DataHandler;
import core.chat.ChatListener;
import core.chat.ChatMessage;
import core.chat.ChatService;
import core.entity.Article;
import core.entity.Comment;
import core.entity.User;
import core.entity.dto.CommentDto;
import core.entity.superficial.SuperficialUser;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TabArticlePanel {

    private JPanel panel;
    private JLabel titleLabel;
    private JLabel updateLabel;
    private JLabel createLabel;
    private JLabel statusLabel;
    private JButton editButton;
    private JLabel editWarnLabel;
    private JPanel leftPanel;
    private JPanel middlePanel;
    private JPanel rightPanel;
    private JPanel contributorPanel;
    private JButton readButton;
    private JPanel commentPanel;
    private JButton commentButton;
    private JButton sendMessageButton;
    private JTextField commentField;
    private JTextField chatField;
    private JPanel chatPanel;
    private JButton addContributorButton;
    private JLabel reloadContributorLabel;
    private JLabel reloadCommentLabel;
    private JScrollPane commentScrollPanel;
    private JScrollPane chatScrollPanel;
    private JScrollPane contributorScrollPanel;
    private JLabel contributorInfoLabel;
    private JLabel commentInfoLabel;
    private JPanel ownerPanel;
    private JButton removeContributorButton;
    private JLabel contributorCountLabel;

    private volatile boolean ownerPermission;
    private volatile boolean contributorPermission;


    private AtomicBoolean reloadCommentClickable;
    private AtomicBoolean reloadContributorClickable;

    private AtomicInteger atomicCommentCount;

    private Paged paged;

    private Article article;

    public TabArticlePanel(Article article, Paged paged) {

        this.paged = paged;
        this.article = article;

        ownerPermission = false;
        contributorPermission = false;

        reloadCommentClickable = new AtomicBoolean(false);
        reloadContributorClickable = new AtomicBoolean(false);

        atomicCommentCount = new AtomicInteger(0);

        $$$setupUI$$$();

        rightPanel.requestFocusInWindow();
        middlePanel.requestFocusInWindow();
        leftPanel.requestFocusInWindow();

        titleLabel.setText(article.getTitle());
        createLabel.setText("Created at : " + TypeConverts.getTimeString(article.getCreated_at()));
        updateLabel.setText("Last update : " + TypeConverts.getTimeString(article.getUpdated_at()));
        statusLabel.setText("Status : " + (article.is_private() ? "Private" : "Public") + " / " + (article.is_released() ? "Published" : "Writing"));
        contributorCountLabel.setText("Contributor count : " + article.getContributors().size());

        populateContributors(article, paged);
        populateChat(article);
        populateComments(article, paged);

        contributorScrollPanel.getVerticalScrollBar().setUnitIncrement(17);
        commentScrollPanel.getVerticalScrollBar().setUnitIncrement(11);
        chatScrollPanel.getVerticalScrollBar().setUnitIncrement(11);

        DataHandler.getDataHandler().getUserAsync(article.getOwner().getId(), false, new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                ownerPanel.add(new OneLineUserPanel(response.getData(), paged).getPanel());
            }
        });

        DataHandler.getDataHandler().getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                filterRegardingPermission(response.getData());
            }
        });

        DataHandler.getDataHandler().getChatService().addSingleListener(article.getId(), new ChatListener() {
            @Override
            public void messageReceiver(ChatMessage chatMessage) {
                addSingleChatMessage(chatMessage);
            }
        });

        sendMessageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (contributorPermission) {
                    sendMessage();
                }
            }
        });

        chatField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (contributorPermission) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        sendMessage();
                    }
                }
            }
        });


        addContributorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ownerPermission) {
                    AddContributorDialog dialog = new AddContributorDialog(TabArticlePanel.this.article, paged);
                    dialog.setVisible(true);
                    DataHandler.getDataHandler().getArticleAsync(article.getId(), false, new DataListener<Article>() {
                        @Override
                        public void onResult(ApiResponse<Article> response) {
                            TabArticlePanel.this.article = response.getData();
                            contributorPanel.removeAll();
                            populateContributors(response.getData(), paged);
                        }
                    });
                }
            }
        });

        removeContributorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (ownerPermission) {
                    RemoveContributorDialog dialog = new RemoveContributorDialog(TabArticlePanel.this.article, paged);
                    dialog.setVisible(true);
                    DataHandler.getDataHandler().getArticleAsync(article.getId(), false, new DataListener<Article>() {
                        @Override
                        public void onResult(ApiResponse<Article> response) {
                            TabArticlePanel.this.article = response.getData();
                            contributorPanel.removeAll();
                            populateContributors(response.getData(), paged);
                        }
                    });
                }
            }
        });

        commentButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                makeComment();
            }
        });

        commentField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    makeComment();
                }
            }
        });

        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ArticleEditDialog articleEditDialog = new ArticleEditDialog(TabArticlePanel.this.article);
                articleEditDialog.setVisible(true);
                editWarnLabel.setText(articleEditDialog.getDialogMessage());
                if (articleEditDialog.getSavedArticle() != null) {
                    TabArticlePanel.this.article = articleEditDialog.getSavedArticle();
                    updateLabel.setText("Last update : " + TypeConverts.getTimeString(articleEditDialog.getSavedArticle().getUpdated_at()));
                    updateLabel.invalidate();
                }
            }
        });

        readButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ArticleReadDialog articleEditDialog = new ArticleReadDialog(TabArticlePanel.this.article);
                articleEditDialog.setVisible(true);
            }
        });

        reloadCommentLabel.setIcon(new ImageIcon(Resources.getImage("reload.png", 20, 20)));
        reloadCommentLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (reloadCommentClickable.get()) {
                    reloadCommentClickable.set(false);
                    commentPanel.removeAll();
                    populateComments(TabArticlePanel.this.article, paged);
                }
            }
        });

        reloadContributorLabel.setIcon(new ImageIcon(Resources.getImage("reload.png", 20, 20)));
        reloadContributorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (reloadContributorClickable.get()) {
                    reloadContributorClickable.set(false);
                    contributorPanel.removeAll();
                    populateContributors(TabArticlePanel.this.article, paged);
                }
            }
        });

    }

    private void populateContributors(Article article, Paged paged) {

        DataHandler dataHandler = DataHandler.getDataHandler();

        int contributorCount = article.getContributors().size();
        ((GridLayout) contributorPanel.getLayout()).setRows((Math.max(contributorCount, 5)));

        AtomicInteger atomicInteger = new AtomicInteger(contributorCount);
        for (SuperficialUser superficialUser : article.getContributors()) {
            dataHandler.getUserAsync(superficialUser.getId(), false, new DataListener<User>() {
                @Override
                public void onStart() {
                    contributorInfoLabel.setText("Reloading ...");
                    contributorInfoLabel.invalidate();
                }

                @Override
                public void onResult(ApiResponse<User> response) {
                    contributorPanel.add(new OneLineUserPanel(response.getData(), paged).getPanel());
                    contributorPanel.invalidate();
                    if (atomicInteger.decrementAndGet() == 0) {
                        reloadContributorClickable.set(true);
                        contributorInfoLabel.setText("Contributor count : " + contributorCount);
                        contributorInfoLabel.invalidate();
                    }
                }
            });
        }
        if (atomicInteger.get() == 0) {
            reloadContributorClickable.set(true);
            contributorInfoLabel.setText("Contributor count : " + contributorCount);
        }
    }

    private void populateChat(Article article) {
        DataHandler dataHandler = DataHandler.getDataHandler();
        List<ChatMessage> chatMessageList = dataHandler.getChatService().getMessages(article.getId());
        ((GridLayout) chatPanel.getLayout()).setRows(Math.max(5, chatMessageList.size()));
        for (ChatMessage chatMessage : chatMessageList) {
            chatPanel.add(new OneLineChatPanel(chatMessage).getPanel());
        }
        chatPanel.invalidate();
    }

    private void addSingleChatMessage(ChatMessage chatMessage) {
        GridLayout gridLayout = (GridLayout) chatPanel.getLayout();
        gridLayout.setRows(Math.max(5, gridLayout.getRows() + 1));
        chatPanel.add(new OneLineChatPanel(chatMessage).getPanel());
        chatPanel.invalidate();
    }

    private void populateComments(Article article, Paged paged) {
        DataHandler dataHandler = DataHandler.getDataHandler();
        dataHandler.getCommentsByArticleAsync(article.getId(), new DataListener<List<Comment>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onResult(ApiResponse<List<Comment>> response) {
                if (response.isConfirmed()) {
                    int commentCount = response.getData().size();
                    atomicCommentCount.set(commentCount);
                    ((GridLayout) commentPanel.getLayout()).setRows(Math.max(5, commentCount));
                    for (Comment comment : response.getData()) {
                        commentPanel.add(new OneLineComment(comment, paged).getPanel());
                    }
                    commentPanel.invalidate();
                    reloadCommentClickable.set(true);
                    commentInfoLabel.setText("Comment count : " + commentCount);
                    commentField.invalidate();
                }
            }
        });
    }

    private void addSingleComment(Comment comment) {
        GridLayout gridLayout = ((GridLayout) commentPanel.getLayout());
        gridLayout.setRows(Math.max(5, atomicCommentCount.incrementAndGet()));
        commentPanel.add(new OneLineComment(comment, paged).getPanel());
        commentPanel.invalidate();
        commentInfoLabel.setText("Comment count : " + atomicCommentCount.get());
        commentInfoLabel.invalidate();
    }


    private void sendMessage() {
        String chatText = chatField.getText().trim();
        if (!chatText.isEmpty()) {
            ChatService chatService = DataHandler.getDataHandler().getChatService();
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessage(chatText);
            chatMessage.setSent_at(System.currentTimeMillis());
            chatMessage.setTo_article_id(article.getId());
            chatMessage.setFrom_user_id(chatService.getUserId());
            chatService.sendChatMessage(chatMessage);
            chatField.setText("");
        }
    }

    private void makeComment() {
        String content = commentField.getText().trim();
        if (!content.isEmpty()) {
            CommentDto commentDto = new CommentDto();
            commentDto.setContent(content);
            commentDto.setArticle_id(TabArticlePanel.this.article.getId());
            DataHandler.getDataHandler().makeCommentAsync(commentDto, new DataListener<Comment>() {
                @Override
                public void onResult(ApiResponse<Comment> response) {
                    if (response.isConfirmed()) {
                        addSingleComment(response.getData());

                    }
                }
            });
            commentField.setText("");
        }
    }


    public JPanel getPanel() {
        return panel;
    }


    private void createUIComponents() {
        contributorPanel = new JPanel(new GridLayout(0, 1));
        commentPanel = new JPanel(new GridLayout(0, 1));
        chatPanel = new JPanel(new GridLayout(0, 1));
        ownerPanel = new JPanel(new GridLayout(1, 1));
    }

    private void filterRegardingPermission(User user) {
        boolean isOwner = controlIfOwner(TabArticlePanel.this.article, user);
        addContributorButton.setVisible(isOwner);
        removeContributorButton.setVisible(isOwner);
        ownerPermission = isOwner;
        boolean havePermission = controlIfHavePermission(TabArticlePanel.this.article, user);
        chatPanel.setVisible(havePermission);
        sendMessageButton.setVisible(havePermission);
        chatScrollPanel.setVisible(havePermission);
        chatField.setVisible(havePermission);
        editButton.setVisible(havePermission);
        readButton.setVisible(havePermission);
        contributorPermission = havePermission;
    }

    private boolean controlIfOwner(Article article, User user) {
        return article.getOwner().getId() == user.getId();
    }

    private boolean controlIfHavePermission(Article article, User user) {
        if (controlIfOwner(article, user)) {
            return true;
        } else {
            return article.getContributors().stream().map(SuperficialUser::getId).anyMatch(id -> id == user.getId());
        }
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
        panel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        titleLabel = new JLabel();
        Font titleLabelFont = this.$$$getFont$$$(null, -1, 35, titleLabel.getFont());
        if (titleLabelFont != null) titleLabel.setFont(titleLabelFont);
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setText("");
        panel.add(titleLabel, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayoutManager(12, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(middlePanel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        updateLabel = new JLabel();
        Font updateLabelFont = this.$$$getFont$$$(null, -1, 18, updateLabel.getFont());
        if (updateLabelFont != null) updateLabel.setFont(updateLabelFont);
        updateLabel.setText("");
        middlePanel.add(updateLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createLabel = new JLabel();
        Font createLabelFont = this.$$$getFont$$$(null, -1, 18, createLabel.getFont());
        if (createLabelFont != null) createLabel.setFont(createLabelFont);
        createLabel.setText("");
        middlePanel.add(createLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusLabel = new JLabel();
        Font statusLabelFont = this.$$$getFont$$$(null, -1, 18, statusLabel.getFont());
        if (statusLabelFont != null) statusLabel.setFont(statusLabelFont);
        statusLabel.setText("");
        middlePanel.add(statusLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editButton = new JButton();
        editButton.setText("Edit");
        editButton.setVisible(false);
        middlePanel.add(editButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        middlePanel.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        readButton = new JButton();
        readButton.setText("Read");
        readButton.setVisible(false);
        middlePanel.add(readButton, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addContributorButton = new JButton();
        addContributorButton.setText("Add Contributor");
        addContributorButton.setVisible(false);
        middlePanel.add(addContributorButton, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editWarnLabel = new JLabel();
        Font editWarnLabelFont = this.$$$getFont$$$(null, -1, 14, editWarnLabel.getFont());
        if (editWarnLabelFont != null) editWarnLabel.setFont(editWarnLabelFont);
        editWarnLabel.setText("");
        middlePanel.add(editWarnLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        middlePanel.add(ownerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        middlePanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        removeContributorButton = new JButton();
        removeContributorButton.setText("Remove Contributor");
        removeContributorButton.setVisible(false);
        middlePanel.add(removeContributorButton, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contributorCountLabel = new JLabel();
        Font contributorCountLabelFont = this.$$$getFont$$$(null, -1, 18, contributorCountLabel.getFont());
        if (contributorCountLabelFont != null) contributorCountLabel.setFont(contributorCountLabelFont);
        contributorCountLabel.setText("");
        middlePanel.add(contributorCountLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(leftPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contributorScrollPanel = new JScrollPane();
        leftPanel.add(contributorScrollPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contributorScrollPanel.setViewportView(contributorPanel);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(rightPanel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        commentScrollPanel = new JScrollPane();
        rightPanel.add(commentScrollPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        commentScrollPanel.setViewportView(commentPanel);
        chatScrollPanel = new JScrollPane();
        rightPanel.add(chatScrollPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        chatScrollPanel.setViewportView(chatPanel);
        commentField = new JTextField();
        rightPanel.add(commentField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chatField = new JTextField();
        rightPanel.add(chatField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        commentButton = new JButton();
        commentButton.setText("Comment");
        rightPanel.add(commentButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendMessageButton = new JButton();
        sendMessageButton.setText("Send");
        rightPanel.add(sendMessageButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadContributorLabel = new JLabel();
        reloadContributorLabel.setText("");
        panel.add(reloadContributorLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadCommentLabel = new JLabel();
        reloadCommentLabel.setText("");
        panel.add(reloadCommentLabel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contributorInfoLabel = new JLabel();
        contributorInfoLabel.setHorizontalAlignment(0);
        contributorInfoLabel.setText("");
        panel.add(contributorInfoLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentInfoLabel = new JLabel();
        commentInfoLabel.setHorizontalAlignment(0);
        commentInfoLabel.setText("");
        panel.add(commentInfoLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
