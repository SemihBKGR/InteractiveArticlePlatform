package app.gui.panel;

import app.gui.dialog.ArticleEditDialog;
import app.gui.dialog.ArticleReadDialog;
import app.gui.dialog.AddContributorDialog;
import app.gui.dialog.RemoveContributorDialog;
import app.util.Paged;
import app.util.Resources;
import app.util.TypeConverts;
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
    private JLabel ownerLabel;
    private JButton removeContributorButton;

    private AtomicBoolean reloadCommentClickable;
    private AtomicBoolean reloadContributorClickable;

    private AtomicInteger atomicCommentCount;

    private Paged paged;

    private Article article;

    public TabArticlePanel(Article article, Paged paged){

        this.paged=paged;
        this.article=article;

        reloadCommentClickable=new AtomicBoolean(false);
        reloadContributorClickable=new AtomicBoolean(false);

        atomicCommentCount=new AtomicInteger(0);

        titleLabel.setText(article.getTitle());
        createLabel.setText("Created at : "+ TypeConverts.getTimeString(article.getCreated_at()));
        updateLabel.setText("Last update : "+TypeConverts.getTimeString(article.getUpdated_at()));
        statusLabel.setText("Status : "+(article.is_private()?"Private":"Public")+" / "+(article.is_released()?"Published":"Writing"));

        ownerLabel.setText("Owner");

        populateContributors(article,paged);
        populateChat(article);
        populateComments(article,paged);

        contributorScrollPanel.getVerticalScrollBar().setUnitIncrement(17);
        commentScrollPanel.getVerticalScrollBar().setUnitIncrement(11);
        chatScrollPanel.getVerticalScrollBar().setUnitIncrement(11);

        DataHandler.getDataHandler().getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                ownerPanel.add(new OneLineUserPanel(response.getData(),paged).getPanel());
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
                sendMessage();
            }
        });

        chatField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendMessage();
                }
            }
        });


        addContributorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddContributorDialog dialog = new AddContributorDialog(TabArticlePanel.this.article,paged);
                dialog.setVisible(true);
                DataHandler.getDataHandler().getArticleAsync(article.getId(),false, new DataListener<Article>() {
                    @Override
                    public void onResult(ApiResponse<Article> response) {
                        TabArticlePanel.this.article=response.getData();
                        contributorPanel.removeAll();
                        populateContributors(response.getData(),paged);
                    }
                });
            }
        });

        removeContributorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RemoveContributorDialog dialog=new RemoveContributorDialog(TabArticlePanel.this.article,paged);
                dialog.setVisible(true);
                DataHandler.getDataHandler().getArticleAsync(article.getId(),false, new DataListener<Article>() {
                    @Override
                    public void onResult(ApiResponse<Article> response) {
                        TabArticlePanel.this.article=response.getData();
                        contributorPanel.removeAll();
                        populateContributors(response.getData(),paged);
                    }
                });
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
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    makeComment();
                }
            }
        });

        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ArticleEditDialog articleEditDialog=new ArticleEditDialog(TabArticlePanel.this.article);
                articleEditDialog.setVisible(true);
                editWarnLabel.setText(articleEditDialog.getDialogMessage());
                if(articleEditDialog.getSavedArticle()!=null){
                    TabArticlePanel.this.article=articleEditDialog.getSavedArticle();
                    updateLabel.setText("Last update : "+TypeConverts.getTimeString(articleEditDialog.getSavedArticle().getUpdated_at()));
                    updateLabel.invalidate();
                }
            }
        });

        readButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ArticleReadDialog articleEditDialog=new ArticleReadDialog(TabArticlePanel.this.article);
                articleEditDialog.setVisible(true);
            }
        });

        reloadCommentLabel.setIcon(new ImageIcon(Resources.getImage("reload.png",20,20)));
        reloadCommentLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(reloadCommentClickable.get()){
                    reloadCommentClickable.set(false);
                    commentPanel.removeAll();
                    populateComments(TabArticlePanel.this.article,paged);
                }
            }
        });

        reloadContributorLabel.setIcon(new ImageIcon(Resources.getImage("reload.png",20,20)));
        reloadContributorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(reloadContributorClickable.get()){
                    reloadContributorClickable.set(false);
                    contributorPanel.removeAll();
                    populateContributors(TabArticlePanel.this.article,paged);
                }
            }
        });

    }

    private void populateContributors(Article article,Paged paged){

        DataHandler dataHandler=DataHandler.getDataHandler();

        int contributorCount=article.getContributors().size();
        ((GridLayout)contributorPanel.getLayout()).setRows((Math.max(contributorCount, 5)));

        AtomicInteger atomicInteger=new AtomicInteger(contributorCount);
        for(SuperficialUser superficialUser:article.getContributors()){
            dataHandler.getUserAsync(superficialUser.getId(),false, new DataListener<User>() {
                @Override
                public void onStart() {
                    contributorInfoLabel.setText("Reloading ...");
                    contributorInfoLabel.invalidate();
                }
                @Override
                public void onResult(ApiResponse<User> response) {
                    contributorPanel.add(new OneLineUserPanel(response.getData(),paged).getPanel());
                    contributorPanel.invalidate();
                    if(atomicInteger.decrementAndGet()==0){
                        reloadContributorClickable.set(true);
                        contributorInfoLabel.setText("Contributor count : "+contributorCount);
                        contributorInfoLabel.invalidate();
                    }
                }
            });
        }
        if(atomicInteger.get()==0){
            reloadContributorClickable.set(true);
            contributorInfoLabel.setText("Contributor count : "+contributorCount);
        }
    }

    private void populateChat(Article article){
        DataHandler dataHandler=DataHandler.getDataHandler();
        java.util.List<ChatMessage> chatMessageList=dataHandler.getChatService().getMessages(article.getId());
        ((GridLayout)chatPanel.getLayout()).setRows(Math.max(5,chatMessageList.size()));
        for(ChatMessage chatMessage:chatMessageList){
            chatPanel.add(new OneLineChatPanel(chatMessage).getPanel());
        }
        chatPanel.invalidate();
    }

    private void addSingleChatMessage(ChatMessage chatMessage){
        GridLayout gridLayout=(GridLayout)chatPanel.getLayout();
        gridLayout.setRows(Math.max(5,gridLayout.getRows()+1));
        chatPanel.add(new OneLineChatPanel(chatMessage).getPanel());
        chatPanel.invalidate();
    }

    private void populateComments(Article article,Paged paged){
        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.getCommentsByArticleAsync(article.getId(), new DataListener<List<Comment>>() {
            @Override
            public void onStart() {

            }
            @Override
            public void onResult(ApiResponse<List<Comment>> response) {
                if(response.isConfirmed()){
                    int commentCount=response.getData().size();
                    atomicCommentCount.set(commentCount);
                    ((GridLayout)commentPanel.getLayout()).setRows(Math.max(5,commentCount));
                    for(Comment comment:response.getData()){
                        commentPanel.add(new OneLineComment(comment,paged).getPanel());
                    }
                    commentPanel.invalidate();
                    reloadCommentClickable.set(true);
                    commentInfoLabel.setText("Comment count : "+commentCount);
                    commentField.invalidate();
                }
            }
        });
    }

    private void addSingleComment(Comment comment){
        GridLayout gridLayout=((GridLayout)commentPanel.getLayout());
        gridLayout.setRows(Math.max(5,atomicCommentCount.incrementAndGet()));
        commentPanel.add(new OneLineComment(comment,paged).getPanel());
        commentPanel.invalidate();
        commentInfoLabel.setText("Comment count : "+atomicCommentCount.get());
        commentInfoLabel.invalidate();
    }


    private void sendMessage(){
        String chatText=chatField.getText().trim();
        if(!chatText.isEmpty()){
            ChatService chatService=DataHandler.getDataHandler().getChatService();
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setMessage(chatText);
            chatMessage.setSent_at(System.currentTimeMillis());
            chatMessage.setTo_article_id(article.getId());
            chatMessage.setFrom_user_id(chatService.getUserId());
            chatService.sendChatMessage(chatMessage);
            chatField.setText("");
        }
    }

    private void makeComment(){
        String content=commentField.getText().trim();
        if(!content.isEmpty()){
            CommentDto commentDto=new CommentDto();
            commentDto.setContent(content);
            commentDto.setArticle_id(TabArticlePanel.this.article.getId());
            DataHandler.getDataHandler().makeCommentAsync(commentDto, new DataListener<Comment>() {
                @Override
                public void onResult(ApiResponse<Comment> response) {
                    if(response.isConfirmed()){
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
        contributorPanel=new JPanel(new GridLayout(0,1));
        commentPanel=new JPanel(new GridLayout(0,1));
        chatPanel=new JPanel(new GridLayout(0,1));
        ownerPanel=new JPanel(new GridLayout(1,1));
    }

    private void filterRegardingPermission(User user){
        boolean isOwner=controlIfOwner(TabArticlePanel.this.article,user);
        addContributorButton.setVisible(isOwner);
        removeContributorButton.setVisible(isOwner);
        boolean havePermission=controlIfHavePermission(TabArticlePanel.this.article,user);
        chatPanel.setVisible(havePermission);
        sendMessageButton.setVisible(havePermission);
        chatScrollPanel.setVisible(havePermission);
        chatField.setVisible(havePermission);
        editButton.setVisible(havePermission);
        readButton.setVisible(havePermission);
    }

    private boolean controlIfOwner(Article article,User user){
        return article.getOwner().getId()==user.getId();
    }

    private boolean controlIfHavePermission(Article article,User user){
        if(controlIfOwner(article,user)){
            return true;
        }else{
            return article.getContributors().stream().map(SuperficialUser::getId).anyMatch(id->id==user.getId());
        }
    }

}
