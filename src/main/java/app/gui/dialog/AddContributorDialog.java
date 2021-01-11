package app.gui.dialog;


import app.gui.panel.OneLineAddContributorPanel;
import app.util.Confirmation;
import app.util.Paged;
import app.util.Resources;
import core.DataHandler;
import core.entity.Article;
import core.entity.User;
import core.entity.superficial.SuperficialUser;
import core.util.ApiResponse;
import core.util.DataListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddContributorDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonCancel;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel userPanel;
    private JLabel warnLabel;

    private AtomicBoolean searchButtonClickable;

    public AddContributorDialog(Article article, Paged paged) {

        setContentPane(contentPane);
        setModal(true);

        searchButtonClickable=new AtomicBoolean(true);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setSize(500,700);
        setIconImage(Resources.getImageIcon("article.png").getImage());

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchButtonClickable.get()){
                    searchButtonClickable.set(false);
                    warnLabel.setText("");
                    userPanel.removeAll();
                    String text= searchField.getText();
                    Confirmation.ConfirmationMessage confirmationMessage=Confirmation.searchTextConfirmation(text);
                    if(confirmationMessage.isConfirmed()){
                        DataHandler.getDataHandler().searchUserAsync(text, new DataListener<List<User>>() {
                            @Override
                            public void onStart() {
                                warnLabel.setText("Searching ...");
                                warnLabel.invalidate();
                            }
                            @Override
                            public void onResult(ApiResponse<List<User>> response) {
                                ((GridLayout)userPanel.getLayout()).setRows(Math.max(5,response.getData().size()));
                                for(User user:response.getData()){
                                    userPanel.add(new OneLineAddContributorPanel(user,article.getId()
                                            ,isContributor(user,article),paged).getPanel());
                                }
                                warnLabel.setText("User result size:"+response.getData().size());
                                searchButtonClickable.set(true);
                            }
                            @Override
                            public void onException(Throwable t) {
                                warnLabel.setText("Something went wrong");
                                searchButtonClickable.set(true);
                            }
                        });
                    }else{
                        warnLabel.setText(confirmationMessage.getMessages());
                    }
                }

            }
        });
    }

    private boolean isContributor(User user,Article article){

        if(user.getOwnArticles().contains(article)){
            return true;
        }
        if(user.getId()!=article.getOwner().getId()){
            for(SuperficialUser contributor :article.getContributors()){
                if(contributor.getId()==user.getId()){
                    return true;
                }
            }
        }else{
            return true;
        }
        return false;
    }


    private void createUIComponents() {
        userPanel=new JPanel(new GridLayout(0,1));
    }

}
