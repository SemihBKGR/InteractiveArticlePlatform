package app.gui.frame;

import app.gui.page.HomePage;
import app.gui.page.ProfilePage;
import app.gui.page.SearchPage;
import app.gui.panel.ButtonPanel;
import app.util.Paged;
import app.util.Resources;
import core.DataHandler;
import core.chat.ChatListener;
import core.chat.ChatMessage;
import core.entity.Article;
import core.entity.User;

import javax.swing.*;
import java.awt.*;

import static app.Contracts.FRAME_TITLE;

public class AppFrame extends JFrame implements Paged {

    private JPanel panel;
    private JPanel centerPanel;
    private JToolBar buttonToolBar;

    private final ProfilePage profilePage;
    private final SearchPage searchPage;
    private final HomePage homePage;

    private ButtonPanel buttonPanel;

    private void createUIComponents() {
        buttonPanel=new ButtonPanel(this,()->{
            profilePage.stop();
            dispose();
            new TransactionFrame().setVisible(true);
        });

        buttonToolBar=buttonPanel.getButtonToolBar();

    }


    public AppFrame(){

        super(FRAME_TITLE);
        setIconImage(Resources.getImageIcon("article.png").getImage());
        setMinimumSize(new Dimension(1000,750));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(panel);

        profilePage=new ProfilePage(this);
        searchPage=new SearchPage(this);
        homePage=new HomePage();

        centerPanel.add(profilePage.getPanel(),ButtonPanel.ActiveButton.profile.toString());
        centerPanel.add(searchPage.getPanel(),ButtonPanel.ActiveButton.search.toString());
        centerPanel.add(homePage.getPanel(),ButtonPanel.ActiveButton.menu.toString());

        profilePage.start();

        DataHandler.getDataHandler().connectWebSocket();
        DataHandler.getDataHandler().loadMessagesAsync();
        DataHandler.getDataHandler().connectChatSocketAsync(chatMessage -> {});

    }

    @Override
    public void changePage(String pageName) {
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();
        cardLayout.show(centerPanel,pageName);
    }

    @Override
    public void changePage(String pageName, Object... items) {
        if(pageName .equals(ButtonPanel.ActiveButton.menu.toString())){
            if(items[0] instanceof Article) {
                homePage.loadArticlePanel((Article)items[0],this);
            }else if(items[0] instanceof User){
                homePage.loadUserPanel((User) items[0],this);
            }
        }
        changePage(pageName);
        buttonPanel.setExplicitlyActive(ButtonPanel.ActiveButton.valueOf(pageName));
    }


}
