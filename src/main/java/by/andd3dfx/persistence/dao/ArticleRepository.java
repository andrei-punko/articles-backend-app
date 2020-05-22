package by.andd3dfx.persistence.dao;

import by.andd3dfx.persistence.entities.Article;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long>, ArticleRepositoryCustom {

}
