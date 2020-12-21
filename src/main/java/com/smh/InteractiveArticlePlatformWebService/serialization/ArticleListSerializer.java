package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.superficial.SuperficialArticle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleListSerializer extends StdSerializer<List<Article>> {

    public ArticleListSerializer(){
        this(null);
    }

    protected ArticleListSerializer(Class<List<Article>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Article> articles,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        List<SuperficialArticle> articleObjectList=new ArrayList<>();
        for(Article article:articles){
            articleObjectList.add(new SuperficialArticle(article));
        }
        jsonGenerator.writeObject(articleObjectList);

    }

}
