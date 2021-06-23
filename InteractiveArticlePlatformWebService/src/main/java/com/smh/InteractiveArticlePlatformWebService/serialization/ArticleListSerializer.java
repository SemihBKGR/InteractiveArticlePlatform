package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.superficial.SuperficialArticle;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Set<SuperficialArticle> articleObjectList=new HashSet<>();
        for(Article article:articles){
            articleObjectList.add(new SuperficialArticle(article));
        }
        jsonGenerator.writeObject(articleObjectList);

    }


}
