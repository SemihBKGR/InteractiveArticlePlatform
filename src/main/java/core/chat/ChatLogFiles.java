package core.chat;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.enums.CloseHandshakeType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class ChatLogFiles {

    private static final File CHAT_LOG_FILE;
    private static final String FOLDER_NAME="chat-logs";
    private static final String LOG_FILE_EXTENSION=".txt";

    static{
        CHAT_LOG_FILE=new File(System.getProperty("user.dir")+"\\"+FOLDER_NAME);
        if(!CHAT_LOG_FILE.exists()){
            CHAT_LOG_FILE.mkdir();
        }
        log.info("ChatLogFile path : "+CHAT_LOG_FILE.getPath());
    }

    private ChatLogFiles(){}


    @SuppressWarnings("unchecked")
    static List<ChatMessage> getMessages(int articleId,int userId) throws IOException, ClassNotFoundException {

        File file=new File(CHAT_LOG_FILE.getAbsolutePath()+"\\"+articleId+LOG_FILE_EXTENSION);
        if(!file.exists()){
            file.createNewFile();
            log.info("File created : "+file.getAbsolutePath());
        }

        file=new File(file.getAbsolutePath()+"\\"+userId);

        if(!file.exists()){
            file.createNewFile();
            log.info("File created : "+file.getAbsolutePath());
            return new ArrayList<>();
        }

        try(ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream(file.getAbsolutePath()+"\\"+articleId+LOG_FILE_EXTENSION))){
            return (List<ChatMessage>) objectInputStream.readObject();
        }

    }

    static void saveMessages(int articleId,int userId,List<ChatMessage> chatMessages) throws IOException {

        if(chatMessages.size()>0){
            File file=new File(CHAT_LOG_FILE.getAbsolutePath()+"\\"+userId+"\\"+articleId+LOG_FILE_EXTENSION);
            try (ObjectOutputStream objectOutputStream=new ObjectOutputStream(new FileOutputStream(file))){
                objectOutputStream.writeObject(chatMessages);
            }
        }

    }

}
