package com.smh.InteractiveArticlePlatformWebService.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.superficial.SuperficialArticle;

import java.io.IOException;

public class ArticleSerializer extends StdSerializer<Article> {

    public ArticleSerializer(){
        this(null);
    }

    protected ArticleSerializer(Class<Article> t) {
        super(t);
    }

    @Override
    public void serialize(Article article,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeObject(new SuperficialArticle(article));

    }
}
