package app;

import app.gui.frame.TransactionFrame;
import app.gui.panel.LoginPanel;
import app.gui.panel.RegisterPanel;
import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args){

        SwingUtilities.invokeLater(()->{
            try {
                UIManager.setLookAndFeel(new DarculaLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new TransactionFrame().setVisible(true);
        });


    }


}
