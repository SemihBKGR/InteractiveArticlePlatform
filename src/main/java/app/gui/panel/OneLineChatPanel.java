package app.gui.panel;

import app.Contracts;
import core.DataHandler;
import core.chat.ChatMessage;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OneLineChatPanel {
    private JPanel panel;
    private JLabel usernameLabel;
    private JLabel messageLabel;

    DataHandler dataHandler;

    public OneLineChatPanel(ChatMessage chatMessage){

        dataHandler=DataHandler.getDataHandler();

        panel.setBorder(new LineBorder(Contracts.DEFAULT_LIGHT_GRAY));

        messageLabel.setText(chatMessage.getMessage());
        dataHandler.getUserAsync(chatMessage.getFrom_user_id(),false,new DataListener<User>() {
            @Override
            public void onResult(ApiResponse<User> response) {

                String pattern="dd-M-yyyy hh:mm";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);

                usernameLabel.setText(response.getData().getUsername()+
                        " ("+dateFormat.format(new Date(chatMessage.getSent_at()))+") : ");
                panel.setVisible(true);

            }
        });

    }

    public JPanel getPanel() {
        return panel;
    }
}
