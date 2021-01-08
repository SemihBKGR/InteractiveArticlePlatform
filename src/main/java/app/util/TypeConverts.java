package app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeConverts {

    public static final String timePattern="dd-M-yyyy hh:mm";
    public static final SimpleDateFormat dateFormat=new SimpleDateFormat(timePattern);

    public static boolean toBoolean (Object object){
        if(object==null){
            return false;
        }
        String value=(String) object;
        return value.equals("true") || value.equals("True");
    }

    public static String toString(Object objects){
        if(objects==null){
            return "";
        }
        return objects.toString();
    }

    public static String getTimeString(long timeMs){
        return getTimeString(new Date(timeMs));
    }

    public static String getTimeString(Date date){
        return dateFormat.format(date);
    }

    public static String getFixedSizeText(String text,int size,String end){

        if(text.length()>size){
            int endLength=end.length();
            return text.substring(0,size-endLength)+end;
        }else if(text.length()<size){
            return text+ " ".repeat(size-text.length());
        }else{
            return text;
        }

    }

}
