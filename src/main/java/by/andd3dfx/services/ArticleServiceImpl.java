package by.andd3dfx.services;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.mappers.ArticleMapper;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.services.exceptions.ArticleNotFoundException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public ArticleDto create(ArticleDto articleDto) {
        Date dateCreated = new Date();
        articleDto.setDateCreated(dateCreated);
        articleDto.setDateUpdated(dateCreated);

        Article entity = articleMapper.toArticle(articleDto);
        Article savedEntity = articleRepository.save(entity);
        return articleMapper.toArticleDto(savedEntity);
        // TODO: returned author with id only after creation. Need to consider, do we need to return author with all fields populated
    }

    @Override
    public void update(Long id, ArticleDto updatedArticleDto) {
        articleRepository.findById(id)
            .map(article -> {
                articleMapper.toArticle(updatedArticleDto, article);
                article.setDateUpdated(new Date());
                Article savedArticle = articleRepository.save(article);
                return articleMapper.toArticleDto(savedArticle);
            }).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Override
    public void delete(Long id) {
        try {
            articleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ArticleNotFoundException(id);
        }
    }

    @Override
    public List<ArticleDto> getAll() {
        List<Article> articles = articleRepository.findAllByOrderByTitle();
        return articleMapper.toArticleDtos(articles);
    }
}
