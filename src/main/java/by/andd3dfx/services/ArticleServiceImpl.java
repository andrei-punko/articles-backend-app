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
        return articleMapper.toArticleDto(
            articleRepository.save(
                articleMapper.toArticle(articleDto)
            ));
    }

    @Override
    public ArticleDto update(Long id, ArticleDto updatedArticleDto) {
        return articleRepository.findById(id)
            .map(article -> {
                if (updatedArticleDto.getTitle() != null) {
                    article.setTitle(updatedArticleDto.getTitle());
                }
                if (updatedArticleDto.getSummary() != null) {
                    article.setSummary(updatedArticleDto.getSummary());
                }
                if (updatedArticleDto.getText() != null) {
                    article.setText(updatedArticleDto.getText());
                }
                article.setDateUpdated(new Date());
                return articleMapper.toArticleDto(
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
        List<Article> articles = articleRepository.findAllByOrderByTitle();
        return articleMapper.toArticleDtos(articles);
    }
}
