package app.gui.page;

import app.gui.panel.OneLineArticlePanel;
import app.gui.panel.OneLineUserPanel;
import app.util.Confirmation;
import app.util.Paged;
import core.DataHandler;
import core.entity.Article;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchPage {

    private static final int GRID_ROW_MIN_COUNT=5;

    private JPanel panel;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel searchInnerPanel;
    private JComboBox searchItemComboBox;
    private JLabel infoLabel;

    private AtomicBoolean searchButtonClickable;

    private SearchItem currentSearchItem;

    private Paged paged;

    private enum SearchItem{

        user("User"),
        article("Article");

        private String searchItem;

        SearchItem(String searchItem){
            this.searchItem=searchItem;
        }

        private String getSearchItem() {
            return searchItem;
        }

        private static SearchItem getByName(String name){
            for(SearchItem item:SearchItem.values()){
                if(item.getSearchItem().equals(name)) return item;
            }
            return getDefault();
        }

        private static SearchItem getDefault(){
            return user;
        }

    }

    public SearchPage(Paged paged){

        this.paged=paged;

        searchButtonClickable=new AtomicBoolean(true);

        currentSearchItem=SearchItem.getDefault();

        for(SearchItem searchItem:SearchItem.values()){
            searchItemComboBox.addItem(searchItem.getSearchItem());
        }
        searchItemComboBox.setSelectedItem(currentSearchItem.getSearchItem());

        searchItemComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                currentSearchItem=SearchItem.getByName((String) e.getItem());
            }
        });


        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchButtonClickable.get()){
                    searchButtonClickable.set(false);
                    String text=searchField.getText().trim();
                    Confirmation.ConfirmationMessage confirmationMessage=Confirmation.searchTextConfirmation(text);
                    if(confirmationMessage.isConfirmed()){
                        searchInnerPanel.removeAll();
                        makeSearch(text);
                    }else{
                        infoLabel.setText(confirmationMessage.getMessages());
                        searchButtonClickable.set(true);
                    }
                }
            }
        });



    }


    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        searchInnerPanel=new JPanel();
        searchInnerPanel.setBorder(new LineBorder(Color.BLACK,3));
        searchInnerPanel.setLayout(new GridLayout(0,1));
    }

    private void makeSearch(String text){

        switch (currentSearchItem){

            case user:

                DataHandler.getDataHandler().searchUserAsync(text, new DataListener<List<User>>() {

                    @Override
                    public void onException(Throwable t) {
                        infoLabel.setText("Something wrong");
                        t.printStackTrace();
                        searchButtonClickable.set(true);
                    }

                    @Override
                    public void onResult(ApiResponse<List<User>> response) {
                        setGridRowCount(response.getData().size());
                        for(User user:response.getData()){
                            searchInnerPanel.add(new OneLineUserPanel(user,paged).getPanel());
                        }
                        setInfoLabelResultText(response.getData().size());
                        searchButtonClickable.set(true);
                    }
                });

                break;

            case article:

                DataHandler.getDataHandler().searchArticleAsync(text, new DataListener<List<Article>>() {
                    @Override
                    public void onException(Throwable t) {
                        infoLabel.setText("Something wrong");
                        t.printStackTrace();
                        searchButtonClickable.set(true);
                    }

                    @Override
                    public void onResult(ApiResponse<List<Article>> response) {
                        setGridRowCount(response.getData().size());
                        for(Article article:response.getData()){
                            searchInnerPanel.add(new OneLineArticlePanel(paged,article).getPanel());
                        }
                        setInfoLabelResultText(response.getData().size());
                        searchButtonClickable.set(true);
                    }
                });

                break;

        }


    }

    private void setInfoLabelResultText(int size){
        infoLabel.setText("Search result size "+size);
    }

    private void setGridRowCount(int size){
        ((GridLayout)searchInnerPanel.getLayout()).setRows(Math.max(size, GRID_ROW_MIN_COUNT));
    }

}
