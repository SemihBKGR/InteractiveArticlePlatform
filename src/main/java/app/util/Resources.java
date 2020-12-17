package app.util;

import javax.swing.*;

public class Resources {

    private final static String ICON_FOLDER_NAME="icons";

    public static ImageIcon getImageIcon(String name){

        return new ImageIcon(Resources.class.getClassLoader()
                .getResource(ICON_FOLDER_NAME).getFile()+"\\"+name);

    }

}
