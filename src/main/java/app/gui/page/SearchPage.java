package app.gui.page;

import app.gui.frame.AppFrame;
import app.gui.panel.OneLineUserPanel;
import core.DataHandler;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;
import org.w3c.dom.CDATASection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchPage {
    private JPanel panel;
    private JTextField searchField;
    private JButton profileButton;
    private JButton searchButton;
    private JPanel searchInnerPanel;

    private AtomicBoolean searchButtonClickable;

    public SearchPage(AppFrame appFrame){

        searchButtonClickable=new AtomicBoolean(true);

        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                appFrame.changePage("profile");
            }
        });

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(searchButtonClickable.get()){
                    searchButtonClickable.set(false);

                    String text=searchField.getText();

                    if(!text.isEmpty()){
                        DataHandler.getDataHandler().searchUserAsync(text, new DataListener<List<User>>() {

                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onException(Throwable t) {
                                searchButtonClickable.set(true);
                                t.printStackTrace();
                            }

                            @Override
                            public void onResult(ApiResponse<List<User>> response) {
                                int responseCount=response.getData().size();
                                ((GridLayout)searchInnerPanel.getLayout()).setRows(responseCount);
                                for(User user:response.getData()){
                                    searchInnerPanel.add(new OneLineUserPanel(user).getPanel());
                                }
                                searchButtonClickable.set(true);
                            }
                        });
                    }

                    searchButtonClickable.set(true);
                }

            }
        });

    }


    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        searchInnerPanel=new JPanel();
        searchInnerPanel.setLayout(new GridLayout(0,1));
    }

}
