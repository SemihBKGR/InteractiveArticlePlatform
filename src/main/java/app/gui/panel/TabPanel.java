package app.gui.panel;

import app.util.ClickListener;
import app.util.Resources;
import app.util.TypeConverts;
import core.entity.Article;
import core.entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TabPanel {

    private static final int TITLE_LENGTH=20;
    private static final String END="...";

    private JPanel panel;

    private JLabel imageLabel;
    private JLabel titleLabel;
    private JLabel closeLabel;


    private TabPanel (ClickListener clickListener){
        titleLabel.setForeground(Color.WHITE);
        closeLabel.setIcon(Resources.getImageIcon("close.png"));
        imageLabel.setBorder(new LineBorder(Color.BLACK,1));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickListener.clicked();
            }
        });
    }

    public TabPanel (Article article,ClickListener clickListener){
        this(clickListener);
        titleLabel.setText(TypeConverts.getFixedSizeText(article.getTitle(),TITLE_LENGTH,END));
        imageLabel.setIcon(Resources.smallestPencilImageIcon);
    }

    public TabPanel(User user,ClickListener clickListener){
        this(clickListener);
        titleLabel.setText(TypeConverts.getFixedSizeText(user.getUsername(),TITLE_LENGTH,END));
        if(user.getInformation().getImage()!=null){
            ByteArrayInputStream imageStream=new ByteArrayInputStream(user.getInformation().getImage());
            try {
                ImageIcon imageIcon=new ImageIcon(Resources.resizeSmallestSize(ImageIO.read(imageStream)));
                imageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
                imageLabel.setIcon(Resources.smallestDefaultImageIcon);
            }
        }else{
            imageLabel.setIcon(Resources.smallestDefaultImageIcon);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

}
