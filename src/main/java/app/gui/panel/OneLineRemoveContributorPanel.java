package app.gui.panel;

import app.Contracts;
import app.util.Paged;
import app.util.Resources;
import core.DataHandler;
import core.entity.Article;
import core.entity.User;
import core.entity.superficial.SuperficialUser;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class OneLineRemoveContributorPanel {

    private JPanel panel;
    private JPanel innerPanel;
    private JButton removeButton;
    private JLabel imageLabel;
    private JLabel usernameLabel;
    private JLabel warnLabel;

    private volatile boolean removeButtonClickable;
    private AtomicBoolean pageClickable;

    public OneLineRemoveContributorPanel(SuperficialUser superficialUser,int articleId, Paged paged){

        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        removeButtonClickable=true;
        pageClickable=new AtomicBoolean(true);

        imageLabel.setBorder(new LineBorder(Color.BLACK));

        usernameLabel.setText(superficialUser.getUsername());
        DataHandler.getDataHandler().getImageAsync(superficialUser.getId(), false, new DataListener<byte[]>() {
            @Override
            public void onException(Throwable t) {
                imageLabel.setIcon(Resources.smallDefaultImageIcon);
            }

            @Override
            public void onResult(ApiResponse<byte[]> response) {
                loadImage(response.getData());
            }
        });

        innerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(pageClickable.get()){
                    pageClickable.set(false);
                    DataHandler.getDataHandler().getUserAsync(superficialUser.getId(), false, new DataListener<User>() {
                        @Override
                        public void onException(Throwable t) {
                            pageClickable.set(true);
                        }
                        @Override
                        public void onResult(ApiResponse<User> response) {
                            paged.changePage(ButtonPanel.ActiveButton.menu.name(),response.getData());
                        }
                    });
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new LineBorder(Contracts.DEFAULT_BLUE,2));
                usernameLabel.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new LineBorder(Contracts.DEFAULT_LIGHT_GRAY));
                usernameLabel.setForeground(Contracts.DEFAULT_WHITE);
            }
        });

        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(removeButtonClickable) {
                    removeButtonClickable=false;
                    DataHandler.getDataHandler().removeContributorAsync(articleId, superficialUser.getId(), new DataListener<Article>() {
                        @Override
                        public void onException(Throwable t) {
                            warnLabel.setText("Something went wrong");
                            removeButtonClickable=true;
                        }
                        @Override
                        public void onResult(ApiResponse<Article> response) {
                            warnLabel.setText(response.getMessage());
                            removeButton.setEnabled(false);
                        }
                    });
                }else {
                    warnLabel.setText("User already removed");
                }
            }
        });

    }


    private void loadImage(byte[] image){
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


    public JPanel getPanel() {
        return panel;
    }
}


