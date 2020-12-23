package app.util;

import com.bulenkov.iconloader.util.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Resources {

    private final static String ICON_FOLDER_NAME="icons";

    public static ImageIcon defaultImageIcon;

    static {
        try {
            defaultImageIcon=new ImageIcon(Scalr.resize
                    (ImageIO.read(new File(Resources.class.getClassLoader().getResource(ICON_FOLDER_NAME).getFile() + "\\default-image.jpg")),
                            Scalr.Method.BALANCED,300,300));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImageIcon getImageIcon(String name){

        return new ImageIcon(Resources.class.getClassLoader()
                .getResource(ICON_FOLDER_NAME).getFile()+"\\"+name);

    }


}
