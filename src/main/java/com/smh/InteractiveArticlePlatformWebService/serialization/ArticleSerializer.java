package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.object.SuperficialArticle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleSerializer extends StdSerializer<List<Article>> {

    public ArticleSerializer(){
        this(null);
    }

    protected ArticleSerializer(Class<List<Article>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Article> articles,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        List<SuperficialArticle> articleObjectList=new ArrayList<>();
        for(Article article:articles){
            articleObjectList.add(convertToSuperficial(article));
        }
        jsonGenerator.writeObject(articleObjectList);

    }

    private SuperficialArticle convertToSuperficial (Article article){

        SuperficialArticle superficialArticle=new SuperficialArticle();
        superficialArticle.setId(article.getId());
        superficialArticle.setTitle(article.getTitle());
        superficialArticle.set_private(article.is_private());
        superficialArticle.set_released(article.is_released());
        superficialArticle.setCreated_at(article.getCreated_at());
        superficialArticle.setUpdated_at(article.getUpdate_at());
        return superficialArticle;

    }

}
