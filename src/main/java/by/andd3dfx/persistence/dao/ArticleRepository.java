package by.andd3dfx.persistence.dao;

import by.andd3dfx.persistence.entities.Article;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    List<Article> findAllByOrderByTitle();
}
