package app.gui.panel;

import app.gui.dialog.ArticleEditDialog;
import app.gui.dialog.ContributorDialog;
import app.util.Paged;
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
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TabArticlePanel {

    private JPanel panel;
    private JLabel titleLabel;
    private JLabel updateLabel;
    private JLabel createLabel;
    private JLabel statusLabel;
    private JButton editButton;
    private JLabel editWarnLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel contributorPanel;
    private JLabel permissionLabel;
    private JButton readButton;
    private JPanel commentPanel;
    private JButton commentButton;
    private JButton sendMessageButton;
    private JTextField commentField;
    private JTextField chatField;
    private JPanel chatPanel;
    private JButton addContributorButton;

    private Paged paged;

    private Article article;

    public TabArticlePanel(Article article, Paged paged){

        this.paged=paged;
        this.article=article;

        titleLabel.setText(article.getTitle());

        String pattern="dd-M-yyyy hh:mm";
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
        createLabel.setText("Created at : "+dateFormat.format(new Date(article.getCreated_at())));
        updateLabel.setText("Last update : "+dateFormat.format(new Date(article.getUpdate_at())));
        statusLabel.setText("Status : "+(article.is_private()?"Private":"Public")+(article.is_released()?"Published":"Writing"));

        populateContributors(article,paged);
        populateChat(article);
        populateComments(article);

        DataHandler.getDataHandler().getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                setButtonsActiveness(article,response.getData());
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
        });


        addContributorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ContributorDialog dialog = new ContributorDialog(TabArticlePanel.this.article,paged);
                dialog.setVisible(true);
                DataHandler.getDataHandler().getArticleAsync(article.getId(), new DataListener<Article>() {
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
                String content=commentField.getText().trim();
                if(!content.isEmpty()){
                    CommentDto commentDto=new CommentDto();
                    commentDto.setContent(content);
                    commentDto.setArticle_id(TabArticlePanel.this.article.getId());
                    DataHandler.getDataHandler().makeCommentAsync(commentDto, new DataListener<Comment>() {
                        @Override
                        public void onResult(ApiResponse<Comment> response) {
                            System.out.println(response);
                            if(response.isConfirmed()){
                                addSingleComment(response.getData());
                            }
                        }
                    });
                    commentField.setText("");
                }
            }
        });

        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ArticleEditDialog articleEditDialog=new ArticleEditDialog(TabArticlePanel.this.article);
                articleEditDialog.setVisible(true);
            }
        });


    }

    private void populateContributors(Article article,Paged paged){

        DataHandler dataHandler=DataHandler.getDataHandler();

        ((GridLayout)contributorPanel.getLayout()).setRows((Math.max(article.getContributors().size() + 1, 5)));

        dataHandler.getUserAsync(article.getOwner().getId(), new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {
                contributorPanel.add(new OneLineUserPanel(response.getData(),paged).getPanel());
                contributorPanel.invalidate();
            }
        });

        for(SuperficialUser superficialUser:article.getContributors()){
            dataHandler.getUserAsync(superficialUser.getId(), new DataListener<User>() {
                @Override
                public void onResult(ApiResponse<User> response) {
                    contributorPanel.add(new OneLineUserPanel(response.getData(),paged).getPanel());
                    contributorPanel.invalidate();
                }
            });
        }

    }

    private void populateChat(Article article){
        DataHandler dataHandler=DataHandler.getDataHandler();
        java.util.List<ChatMessage> chatMessageList=dataHandler.getChatService().getMessages(article.getId());
        ((GridLayout)chatPanel.getLayout()).setRows(Math.max(5,chatMessageList.size()));
        for(ChatMessage chatMessage:chatMessageList){
            chatPanel.add(new OneLineChatPanel(chatMessage).getPanel());
        }
    }

    private void addSingleChatMessage(ChatMessage chatMessage){
        GridLayout gridLayout=(GridLayout)chatPanel.getLayout();
        gridLayout.setRows(Math.max(5,gridLayout.getRows()+1));
        chatPanel.add(new OneLineChatPanel(chatMessage).getPanel());
    }

    private void populateComments(Article article){
        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.getCommentsByArticleAsync(article.getId(), new DataListener<List<Comment>>() {
            @Override
            public void onResult(ApiResponse<List<Comment>> response) {
                if(response.isConfirmed()){
                    ((GridLayout)commentPanel.getLayout()).setRows(Math.max(5,response.getData().size()));
                    for(Comment comment:response.getData()){
                        commentPanel.add(new OneLineComment(comment,paged).getPanel());
                    }
                }
            }
        });
    }

    private void addSingleComment(Comment comment){
        GridLayout gridLayout=((GridLayout)commentPanel.getLayout());
        gridLayout.setRows(Math.max(5,gridLayout.getRows()+1));
        commentPanel.add(new OneLineComment(comment,paged).getPanel());
    }



    public JPanel getPanel() {
        return panel;
    }


    private void createUIComponents() {
        contributorPanel=new JPanel(new GridLayout(0,1));

        commentPanel=new JPanel(new GridLayout(0,1));
        chatPanel=new JPanel(new GridLayout(0,1));

    }

    private void setButtonsActiveness(Article article,User user){

        if(user.getOwnArticles().stream().anyMatch(article1 -> article1.getId()==article.getId())
                || user.getContributorArticle().stream().anyMatch(article1 -> article1.getId()==article.getId())){
            editButton.setVisible(true);
            readButton.setVisible(true);
            addContributorButton.setVisible(true);
        }

    }

    private void filterRegardingPermission(){

        DataHandler.getDataHandler().getMeAsync(new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {

            }
        });

    }


}
