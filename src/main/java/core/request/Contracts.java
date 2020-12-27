package core.request;

class Contracts {

    static final String BASE_URL="http://localhost:8080";

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


    //Article
    static final String ARTICLE_URL=BASE_URL+"/article";

    static final String ARTICLE_GET_URL=ARTICLE_URL+"/get/id";
    static final String ARTICLE_CREATE_URL=ARTICLE_URL+"/create";
    static final String ARTICLE_SEARCH_URL=ARTICLE_URL+"/search";

}
