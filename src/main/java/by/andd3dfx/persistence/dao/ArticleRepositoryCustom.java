package by.andd3dfx.persistence.dao;

import by.andd3dfx.dto.ArticleSearchCriteria;
import by.andd3dfx.dto.DeleteArticleSearchCriteria;
import by.andd3dfx.persistence.entities.Article;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepositoryCustom {

    List<Article> findByCriteria(ArticleSearchCriteria criteria);

    boolean existsByCriteria(ArticleSearchCriteria criteria);

    void deleteByCriteria(DeleteArticleSearchCriteria criteria);
}
