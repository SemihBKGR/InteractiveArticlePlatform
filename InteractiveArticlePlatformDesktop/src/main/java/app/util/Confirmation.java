package app.util;

import java.util.regex.Pattern;

public class Confirmation {

    private final static String USERNAME_REGEX_PATTERN="[a-zA-Z0-9_.-]{0,}";
    private final static int USERNAME_MIN_LENGTH=5;
    private final static int USERNAME_MAX_LENGTH=25;

    private final static String PASSWORD_REGEX_PATTERN="[a-zA-Z0-9_.-]{0,}";
    private final static int PASSWORD_MIN_LENGTH=7;
    private final static int PASSWORD_MAX_LENGTH=27;

    private final static String EMAIL_REGEX_PATTERN="^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private final static String TITLE_REGEX_PATTERN="^[a-zA-Z0-9\\s]+$";
    private final static int TITLE_MIN_LENGTH=5;
    private final static int TITLE_MAX_LENGTH=50;

    private final static String SEARCH_REGEX_PATTERN="[a-zA-Z0-9_.-]{0,}";
    private final static int SEARCH_MIN_LENGTH=1;
    private final static int SEARCH_MAX_LENGTH=25;

    public static class ConfirmationMessage{

        private StringBuilder messages;
        private boolean isConfirmed;
        private boolean htmlMessage;
        private String split;

        private ConfirmationMessage(boolean htmlMessage){
            this.htmlMessage=htmlMessage;
            isConfirmed=true;
            split=htmlMessage?"<br>":"\n";
            messages=new StringBuilder();
            if(htmlMessage){
                messages.append("<html>");
            }
        }

        public void addMessage(String message){
            if(message!=null) {
                messages.append(message);
                messages.append(split);
            }
        }

        public String getMessages(){
            return messages.toString();
        }

        public boolean isConfirmed() {
            return isConfirmed;
        }

        public void setConfirmed(boolean confirmed) {
            isConfirmed = confirmed;
        }

    }


    public static ConfirmationMessage usernameConfirmation(String username){

        ConfirmationMessage confirmationMessage=new ConfirmationMessage(true);

        Pattern pattern=Pattern.compile(USERNAME_REGEX_PATTERN);

        if(!pattern.matcher(username).matches() && username.length()>0){
            confirmationMessage.addMessage("* Contains invalid character");
            confirmationMessage.setConfirmed(false);
        }

        if(username.length()< USERNAME_MIN_LENGTH || username.length()>USERNAME_MAX_LENGTH){
            confirmationMessage.addMessage("* Must be between "+USERNAME_MIN_LENGTH+"-"+USERNAME_MAX_LENGTH);
            confirmationMessage.setConfirmed(false);
        }

        return confirmationMessage;

    }

    public static ConfirmationMessage passwordConfirmation(String password){

        ConfirmationMessage confirmationMessage=new ConfirmationMessage(true);

        Pattern pattern=Pattern.compile(PASSWORD_REGEX_PATTERN);

        if(!pattern.matcher(password).matches() && password.length()>0){
            confirmationMessage.addMessage("* Contains invalid character");
            confirmationMessage.setConfirmed(false);
        }

        if(password.length()< USERNAME_MIN_LENGTH || password.length()>USERNAME_MAX_LENGTH){
            confirmationMessage.addMessage("* Must be between"+PASSWORD_MIN_LENGTH+"-"+PASSWORD_MAX_LENGTH);
            confirmationMessage.setConfirmed(false);
        }

        return confirmationMessage;

    }

    public static ConfirmationMessage emailConfirmation(String email){

        ConfirmationMessage confirmationMessage=new ConfirmationMessage(true);

        Pattern pattern=Pattern.compile(EMAIL_REGEX_PATTERN);

        if(!pattern.matcher(email).matches()){
            confirmationMessage.addMessage("* Not email");
            confirmationMessage.setConfirmed(false);
        }

        return confirmationMessage;

    }

    public static ConfirmationMessage articleTitleConfirmation(String title){

        ConfirmationMessage confirmationMessage=new ConfirmationMessage(true);
        Pattern pattern=Pattern.compile(TITLE_REGEX_PATTERN);

        if(!pattern.matcher(title).matches()){
            confirmationMessage.addMessage("Contains invalid character");
            confirmationMessage.setConfirmed(false);
        }

        if(title.length()>TITLE_MAX_LENGTH ||title.length()<TITLE_MIN_LENGTH){
            confirmationMessage.addMessage("Title must be between "+TITLE_MIN_LENGTH+" - "+TITLE_MAX_LENGTH);
            confirmationMessage.setConfirmed(false);
        }

        return confirmationMessage;

    }


    public static ConfirmationMessage searchTextConfirmation(String text){

        ConfirmationMessage confirmationMessage=new ConfirmationMessage(true);
        Pattern pattern=Pattern.compile(SEARCH_REGEX_PATTERN);

        if(!pattern.matcher(text).matches()){
            confirmationMessage.addMessage("Search text contains invalid character");
            confirmationMessage.setConfirmed(false);
        }

        if(text.length()>SEARCH_MAX_LENGTH || text.length()<SEARCH_MIN_LENGTH){
            confirmationMessage.addMessage("Search text must be between "+SEARCH_MIN_LENGTH+" - "+SEARCH_MAX_LENGTH);
            confirmationMessage.setConfirmed(false);
        }

        return confirmationMessage;

    }


}
