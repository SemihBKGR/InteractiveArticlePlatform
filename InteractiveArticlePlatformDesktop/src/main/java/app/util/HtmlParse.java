package app.util;

public class HtmlParse {

    public static String convertToHtml(String s){
        StringBuilder result=new StringBuilder();
        result.append("<html>");
        String[] lines=s.split("\n");
        for(int i=0;i<lines.length;i++){
            result.append(lines[i]);
            if(i!=lines.length-1){
                result.append("<br>");
            }
        }
        return result.toString();
    }

}
