package core.request;

import com.fasterxml.jackson.databind.introspect.VirtualAnnotatedMember;

public class Contracts {

    private static final String HOST;

    static {
        if(System.getProperty("host")!=null){
            HOST=System.getProperty("host");
        }else{
            HOST="localhost";
        }
    }


    public static final String BASE_URL="http://"+HOST+":8080";

    //Transaction
    static final String LOGIN_URL=BASE_URL+"/loginControl";
    static final String REGISTER_URL=BASE_URL+"/register";


    //User
    static final String USER_URL=BASE_URL+"/user";

    static final String ME_URL=USER_URL+"/get/me";
    static final String USER_GET_URL=USER_URL+"/get/id";
    static final String USER_SEARCH_URL=USER_URL+"/search";

    //Information
    static final String INFORMATION_URL=BASE_URL+"/information";

    static final String INFORMATION_SAVE_URL =INFORMATION_URL+"/save";
    static final String INFORMATION_GET_IMAGE_URL=INFORMATION_URL+"/image/get";

    //Article
    static final String ARTICLE_URL=BASE_URL+"/article";

    static final String ARTICLE_GET_URL=ARTICLE_URL+"/get/id";
    static final String ARTICLE_CREATE_URL=ARTICLE_URL+"/create";
    static final String ARTICLE_SEARCH_URL=ARTICLE_URL+"/search";
    static final String ARTICLE_SAVE_URL=ARTICLE_URL+"/save";
    static final String ARTICLE_ADD_CONTRIBUTOR_URL=ARTICLE_URL+"/contributor/add";
    static final String ARTICLE_REMOVE_CONTRIBUTOR_URL=ARTICLE_URL+"/contributor/remove";

    //Message
    static final String MESSAGE_URL=BASE_URL+"/message";

    static final String MESSAGE_GET_URL=MESSAGE_URL+"/get";

    //Comment
    static final String COMMENT_URL= BASE_URL+"/comment";

    static final String COMMENT_MAKE_URL=COMMENT_URL+"/make";
    static final String COMMENT_GET_BY_ARTICLE_URL=COMMENT_URL+"/get/article";

}
