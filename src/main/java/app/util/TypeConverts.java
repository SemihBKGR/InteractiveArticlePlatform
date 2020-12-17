package app.util;

import java.util.Objects;

public class TypeConverts {

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


}
