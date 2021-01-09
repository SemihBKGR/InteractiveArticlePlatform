package app;

import app.gui.frame.TransactionFrame;
import app.util.Settings;
import com.bulenkov.darcula.DarculaLaf;
import core.DataHandler;
import core.DataPolicy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args){

        //-Dsun.java2d.uiScale=1.0
        Logger.getRootLogger().setLevel(Level.INFO);

        DataHandler.initialize(DataPolicy.getPolicyBySystemFeatures());

        try {
            Settings.readSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(()->{

            new TransactionFrame().setVisible(true);

        });

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                Settings.writeSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                DataHandler.getDataHandler().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));



    }

}

