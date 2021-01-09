package app.gui.panel;

import app.Contracts;
import app.util.LogoutListener;
import app.util.Paged;
import lombok.extern.log4j.Log4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Log4j
public class ButtonPanel {

    private JButton logoutButton;
    private JButton profileButton;
    private JButton searchButton;
    private JButton menuButton;
    private JToolBar buttonToolBar;

    public enum ActiveButton{

        logout,
        profile,
        search,
        menu;

        static ActiveButton getDefault(){
            return profile;
        }

    }

    private ActiveButton activeButton;

    public ButtonPanel (Paged paged, LogoutListener logoutListener){

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(activeButton!=ActiveButton.logout){
                    log.info("ButtonPanel clicked logout");
                    activeButton=ActiveButton.logout;
                    setButtonForeColor();
                    logoutListener.logout();
                }
            }
        });

        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(activeButton!=ActiveButton.profile){
                    log.info("ButtonPanel clicked profile");
                    activeButton=ActiveButton.profile;
                    setButtonForeColor();
                    paged.changePage(activeButton.name());
                }
            }
        });

        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(activeButton!=ActiveButton.menu){
                    log.info("ButtonPanel clicked home");
                    activeButton=ActiveButton.menu;
                    setButtonForeColor();
                    paged.changePage(activeButton.name());
                }
            }
        });

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(activeButton!=ActiveButton.search){
                    log.info("ButtonPanel clicked search");
                    activeButton=ActiveButton.search;
                    setButtonForeColor();
                    paged.changePage(activeButton.name());
                }
            }
        });

        activeButton=ActiveButton.getDefault();
        setButtonForeColor();

    }

    public void setButtonForeColor(){

        menuButton.setForeground(Color.WHITE);
        searchButton.setForeground(Color.WHITE);
        profileButton.setForeground(Color.WHITE);
        logoutButton.setForeground(Color.WHITE);

        if(activeButton==ActiveButton.menu){
            menuButton.setForeground(Contracts.DEFAULT_BLUE);
        }else if(activeButton==ActiveButton.logout){
            logoutButton.setForeground(Contracts.DEFAULT_BLUE);
        }else if(activeButton==ActiveButton.search){
            searchButton.setForeground(Contracts.DEFAULT_BLUE);
        }else{
            profileButton.setForeground(Contracts.DEFAULT_BLUE);
        }

    }

    public void setExplicitlyActive(ActiveButton active){
        activeButton=active;
        setButtonForeColor();
    }


    public JToolBar getButtonToolBar() {
        return buttonToolBar;
    }

}
