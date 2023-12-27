package by.andd3dfx.persistence.dao;

import by.andd3dfx.persistence.entities.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long>, ArticleRepositoryCustom {

    Slice<Article> findAll(Pageable pageable);
}
