package app.gui.panel;

import app.util.Paged;
import app.util.Resources;
import core.DataHandler;
import core.entity.Article;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class OneLineContributorPanel {

    private JLabel usernameLabel;
    private JButton addButton;
    private JPanel panel;
    private JLabel imageLabel;
    private JPanel innerPanel;
    private JLabel warnLabel;

    private AtomicBoolean clicked;

    public OneLineContributorPanel(User user,int articleId,boolean isContributor, Paged paged){

        usernameLabel.setText(user.getUsername());
        imageLabel.setBorder(new LineBorder(Color.BLACK,2));
        loadImage(user.getInformation().getImage());

        panel.setBorder(new LineBorder(Color.BLACK,1));

        clicked=new AtomicBoolean(false);
        addButton.setEnabled(!isContributor);

        innerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paged.changePage(ButtonPanel.ActiveButton.menu.name(),user);
            }
        });

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!clicked.get()){
                    clicked.set(true);
                    DataHandler.getDataHandler().addContributorAsync(articleId, user.getId(),new DataListener<Article>(){
                        @Override
                        public void onException(Throwable t) {
                            addButton.setEnabled(false);
                            warnLabel.setText("Something went wrong");
                        }
                        @Override
                        public void onResult(ApiResponse<Article> response) {
                            if(response.isConfirmed()){
                                warnLabel.setText(response.getMessage());
                                addButton.setEnabled(false);
                            }else{
                                warnLabel.setText(response.getMessage());
                                clicked.set(false);
                            }
                        }
                    });
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
