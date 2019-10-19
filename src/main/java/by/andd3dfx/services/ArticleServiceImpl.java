package by.andd3dfx.services;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.mappers.ArticleMapper;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.services.exceptions.ArticleNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
        return articleMapper.toArticleDtos(
            articleRepository.save(
                articleMapper.toArticle(articleDto)
            ));
    }

    @Override
    public ArticleDto update(Long id, ArticleDto updatedArticleDto) {
        return articleRepository.findById(id)
            .map(article -> {
                article.setTitle(updatedArticleDto.getTitle());
                article.setSummary(updatedArticleDto.getSummary());
                article.setText(updatedArticleDto.getText());
                article.setDateUpdated(new Date());
                return articleMapper.toArticleDtos(
                    articleRepository.save(article)
                );
            })
            .orElseThrow(() -> new ArticleNotFoundException(id));
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
        Iterable<Article> articleIterable = articleRepository.findAllByOrderByTitle();
        List<Article> entities = StreamSupport
            .stream(articleIterable.spliterator(), false)
            .collect(Collectors.toList());
        return articleMapper.toArticleDtos(entities);
    }
}
