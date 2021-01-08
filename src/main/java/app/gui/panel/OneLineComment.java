package app.gui.panel;

import app.util.Paged;
import app.util.Resources;
import app.util.TypeConverts;
import core.DataHandler;
import core.entity.Comment;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class OneLineComment {

    private JPanel panel;
    private JLabel usernameLabel;
    private JLabel contentLabel;
    private JLabel imageLabel;

    public OneLineComment(Comment comment, Paged paged) {

        usernameLabel.setText(comment.getUser().getUsername() + " (" + TypeConverts.getTimeString(comment.getCreated_at()) + ") :");
        contentLabel.setText(comment.getContent());

        imageLabel.setBorder(new LineBorder(Color.BLACK,2));

        DataHandler.getDataHandler().getImageByUserIdAsync(comment.getUser().getId(), new DataListener<byte[]>() {
            @Override
            public void onResult(ApiResponse<byte[]> response) {
                byte[] image=response.getData();
                if(image!=null){
                    ByteArrayInputStream imageStream=new ByteArrayInputStream(image);
                    try {
                        ImageIcon imageIcon=new ImageIcon(Resources.resizeSmallSize(ImageIO.read(imageStream)));
                        imageLabel.setIcon(imageIcon);
                    } catch (IOException e) {
                        e.printStackTrace();
                        imageLabel.setIcon(Resources.smallDefaultImageIcon);
                    }
                }else{
                    imageLabel.setIcon(Resources.smallDefaultImageIcon);
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataHandler.getDataHandler().getUserAsync(comment.getUser().getId(), new DataListener<User>() {
                    @Override
                    public void onResult(ApiResponse<User> response){
                        if(response.isConfirmed()){
                            paged.changePage(ButtonPanel.ActiveButton.menu.name(),response.getData());

                        }
                    }
                });
            }
        });

    }


    public JPanel getPanel() {
        return panel;
    }

}
