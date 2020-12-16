package app.util;

import java.util.regex.Pattern;

public class Confirmation {

    private final static String USERNAME_REGEX_PATTERN="[a-zA-Z0-9_.-]";
    private final static int USERNAME_MIN_LENGTH=5;
    private final static int USERNAME_MAX_LENGTH=25;

    private final static String PASSWORD_REGEX_PATTERN="[a-zA-Z0-9_.-]";
    private final static int PASSWORD_MIN_LENGTH=7;
    private final static int PASSWORD_MAX_LENGTH=27;

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
            confirmationMessage.addMessage("* Must be between"+USERNAME_MIN_LENGTH+"-"+USERNAME_MAX_LENGTH);
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


}
