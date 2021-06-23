package app.util;

public interface Paged {

    void changePage(String pageName);
    void changePage(String pageName,Object ... items);

}
