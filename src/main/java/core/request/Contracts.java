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


    //Article
    static final String ARTICLE_URL=BASE_URL+"/article";

    static final String ARTICLE_GET_URL=ARTICLE_URL+"/get/id";


}
