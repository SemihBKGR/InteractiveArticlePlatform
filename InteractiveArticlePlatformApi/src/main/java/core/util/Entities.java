package core.util;

import core.entity.Article;
import core.entity.superficial.SuperficialArticle;

public class Entities {

    public static SuperficialArticle articleToSuperficialArticle(Article article){

        SuperficialArticle superficialArticle=new SuperficialArticle();
        superficialArticle.setId(article.getId());
        superficialArticle.setTitle(article.getTitle());
        superficialArticle.setOwner_id(article.getOwner().getId());
        superficialArticle.setOwner_name(article.getOwner().getUsername());
        superficialArticle.setOwner_email(article.getOwner().getEmail());
        superficialArticle.setContributor_count(article.getContributors().size());
        superficialArticle.set_private(article.is_private());
        superficialArticle.set_released(article.is_released());
        superficialArticle.setCreated_at(article.getCreated_at());
        superficialArticle.setUpdated_at(article.getUpdated_at());
        return superficialArticle;

    }


}
