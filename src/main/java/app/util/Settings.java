package app.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class Settings {

    private static final String SETTING_FILE="setting.properties";
    private static final Properties properties;

    public static final String REMEMBER_ME="rememberme";
    public static final String USERNAME="username";
    public static final String PASSWORD="password";

    static {
        properties=new Properties();
    }

    public static void readSettings() throws IOException {
        properties.load(Settings.class.getClassLoader().getResourceAsStream(SETTING_FILE));
        log.info("Settings read");
    }

    public static void writeSettings() throws IOException {
        setRelatedSettings();
        properties.store(new FileOutputStream(Settings.class.getClassLoader().getResource(SETTING_FILE).getFile()),"Settings");
        log.info("Settings written");
    }

    public static Object getSetting(String name){
        return properties.get(name);
    }

    public static void setSetting(String name,Object value){
        properties.setProperty(name,value.toString());
    }

    public static void removeSetting(String name){
        properties.remove(name);
    }

    private static void setRelatedSettings(){
        if(!TypeConverts.toBoolean(properties.getProperty(REMEMBER_ME))){
            properties.remove(USERNAME);
            properties.remove(PASSWORD);
        }else{

            if(TypeConverts.toString(properties.getProperty(USERNAME)).equals("")
                    || TypeConverts.toString(properties.getProperty(PASSWORD)).equals("")){

                properties.setProperty(REMEMBER_ME,((Object)false).toString());

            }

        }
    }

}
