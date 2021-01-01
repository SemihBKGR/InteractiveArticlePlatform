package app.gui.frame;

import app.gui.page.HomePage;
import app.gui.page.ProfilePage;
import app.gui.page.SearchPage;
import app.util.Resources;
import core.DataHandler;
import core.entity.Article;
import core.entity.User;

import javax.swing.*;

import java.awt.*;

import static app.Contracts.FRAME_TITLE;

public class AppFrame extends JFrame{

    private JPanel panel;
    private JPanel centerPanel;

    private final ProfilePage profilePage;
    private final SearchPage searchPage;
    private final HomePage homePage;

    public enum Page{

        profile("profile"),
        search("search"),
        home("home");

        private String name;

        Page(String name) {
            this.name = name;
        }

        public String getName(){
            return name;
        }

        public static Page getDefault(){
            return profile;
        }

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
        homePage=new HomePage(this);

        centerPanel.add(profilePage.getPanel(),Page.profile.name);
        centerPanel.add(searchPage.getPanel(),Page.search.name);
        centerPanel.add(homePage.getPanel(),Page.home.name);

        profilePage.start();

    }

    public void changePage(Page page){
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();
        cardLayout.show(centerPanel,page.name);
    }

    public void changePage(Page page,Object ... o){
        if(page== Page.home){
            if(o[0] instanceof Article) {
                homePage.loadArticlePanel((Article)o[0]);
            }
        }
        changePage(page);
    }

}
