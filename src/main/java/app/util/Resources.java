package app.util;

import com.bulenkov.iconloader.util.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static app.Contracts.*;

public class Resources {

    private final static String ICON_FOLDER_NAME="icons";

    public static ImageIcon defaultImageIcon;
    public static ImageIcon smallDefaultImageIcon;
    public static ImageIcon smallestDefaultImageIcon;
    public static ImageIcon smallestPencilImageIcon;

    static {
        try {
            defaultImageIcon=new ImageIcon(Scalr.resize
                    (ImageIO.read(new File(Resources.class.getClassLoader().getResource(ICON_FOLDER_NAME).getFile() + "\\default-image.jpg")),
                            Scalr.Method.QUALITY,IMAGE_SIZE,IMAGE_SIZE));
            smallDefaultImageIcon=new ImageIcon(Scalr.resize
                    (ImageIO.read(new File(Resources.class.getClassLoader().getResource(ICON_FOLDER_NAME).getFile() + "\\default-image.jpg")),
                            Scalr.Method.QUALITY,IMAGE_SMALL_SIZE,IMAGE_SMALL_SIZE));
            smallestDefaultImageIcon=new ImageIcon(Scalr.resize
                    (ImageIO.read(new File(Resources.class.getClassLoader().getResource(ICON_FOLDER_NAME).getFile() + "\\default-image.jpg")),
                            Scalr.Method.QUALITY,IMAGE_SMALLEST_SIZE,IMAGE_SMALLEST_SIZE));
            smallestPencilImageIcon=new ImageIcon(Scalr.resize
                    (ImageIO.read(new File(Resources.class.getClassLoader().getResource(ICON_FOLDER_NAME).getFile() + "\\pencil.png")),
                            Scalr.Method.QUALITY,IMAGE_SMALLEST_SIZE,IMAGE_SMALLEST_SIZE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageIcon getImageIcon(String name){

        return new ImageIcon(Resources.class.getClassLoader()
                .getResource(ICON_FOLDER_NAME).getFile()+"\\"+name);

    }

    public static BufferedImage cropAndResizeDefaultSize(BufferedImage image){
        int width=image.getWidth();
        int height=image.getHeight();
        if(width>height){
            image=Scalr.crop(image,(width-height)/2,0,height,height);
        }else if(height>width) {
            image=Scalr.crop(image,0,(height-width)/2,width,width);
        }
        return Scalr.resize(image,Scalr.Method.QUALITY,IMAGE_SIZE,IMAGE_SIZE);
    }

    public static BufferedImage resizeSmallSize(BufferedImage image){
        return Scalr.resize(image,Scalr.Method.QUALITY,IMAGE_SMALL_SIZE,IMAGE_SMALL_SIZE);
    }

    public static BufferedImage resizeSmallestSize(BufferedImage image){
        return Scalr.resize(image,Scalr.Method.QUALITY,IMAGE_SMALLEST_SIZE,IMAGE_SMALLEST_SIZE);
    }

}
