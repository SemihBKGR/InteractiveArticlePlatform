package app.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

@Slf4j
public class Settings {

    private static final String SETTING_FILE="/settings.properties";

    private static SecretKeySpec secretKey;
    private static byte[] key;

    private static final Properties properties;

    public static final String REMEMBER_ME="rememberme";
    public static final String USERNAME="username";
    public static final String PASSWORD="password";

    static {
        properties=new Properties();
        try {
            MessageDigest sha=MessageDigest.getInstance("SHA-1");
            key="SecretKey".getBytes("UTF-8");
            key = sha.digest(key);
            key= Arrays.copyOf(key,16);
        } catch (UnsupportedEncodingException e) {
            key= Arrays.copyOf("SecretKey".getBytes(),16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        secretKey = new SecretKeySpec(key, "AES");
    }

    public static void readSettings() throws IOException {
        File file=new File(System.getProperty("user.dir")+SETTING_FILE);
        if(file.exists()){
            properties.load(new FileInputStream(file));
            log.info("Settings read");
        }else{
            file.createNewFile();
            log.info("Settings file created");
        }
    }

    public static void writeSettings() throws IOException {
        setRelatedSettings();
        properties.store(new FileOutputStream(new File(System.getProperty("user.dir")+SETTING_FILE)),"SETTINGS");
        log.info("Settings written");
    }

    public static Object getSetting(String name){
        Object encryptedProperty=properties.get(name);
        return decrypt(encryptedProperty);
    }

    public static void setSetting(String name,Object value){
        properties.setProperty(name,encrypt(value));
    }

    public static void removeSetting(String name){
        properties.remove(name);
    }

    private static void setRelatedSettings(){
        if(!TypeConverts.toBoolean(decrypt(properties.getProperty(REMEMBER_ME)))){
            properties.remove(USERNAME);
            properties.remove(PASSWORD);
        }else{

            if(TypeConverts.toString(properties.getProperty(USERNAME)).equals("")
                    || TypeConverts.toString(properties.getProperty(PASSWORD)).equals("")){

                properties.setProperty(REMEMBER_ME,((Object)false).toString());

            }

        }
    }

    private static String encrypt(Object data)  {
        if(data==null){
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.toString().getBytes("UTF-8")));
        } catch (Exception e) {
            return data.toString();
        }
    }

    private static String decrypt(Object data){
        if(data==null){
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.toString())));
        } catch (Exception e) {
            return data.toString();
        }
    }


}
