package by.andd3dfx.services;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.mappers.ArticleMapper;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.services.exceptions.ArticleNotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final Clock clock;

    @Override
    @CacheEvict(value = "allArticles", allEntries = true)
    public ArticleDto create(ArticleDto articleDto) {
        LocalDateTime dateCreated = LocalDateTime.now(clock);
        articleDto.setDateCreated(dateCreated);
        articleDto.setDateUpdated(dateCreated);

        Article entity = articleMapper.toArticle(articleDto);
        Article savedEntity = articleRepository.save(entity);
        return articleMapper.toArticleDto(savedEntity);
        // TODO: returned author with id only after creation. Need to consider, do we need to return author with all fields populated
    }

    @Override
    @Cacheable(value = "articles", key = "#id")
    public ArticleDto get(Long id) {
        return articleRepository.findById(id)
            .map(articleMapper::toArticleDto)
            .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "articles", key = "#id"),
            @CacheEvict(value = "allArticles", allEntries = true)
        }
    )
    public void update(Long id, ArticleUpdateDto articleUpdateDto) {
        articleRepository.findById(id)
            .map(article -> {
                articleMapper.toArticle(articleUpdateDto, article);
                article.setDateUpdated(LocalDateTime.now(clock));
                Article savedArticle = articleRepository.save(article);
                return articleMapper.toArticleDto(savedArticle);
            }).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = "articles", key = "#id"),
            @CacheEvict(value = "allArticles", allEntries = true)
        }
    )
    public void delete(Long id) {
        try {
            articleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ArticleNotFoundException(id);
        }
    }

    @Override
    @Cacheable(value = "allArticles", key = "{#pageNo, #pageSize, #sortBy}")
    public List<ArticleDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Article> pagedResult = articleRepository.findAll(pageRequest);
        return articleMapper.toArticleDtoList(pagedResult.getContent());
    }

    @Override
    @Cacheable(value = "allArticles", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public Page<ArticleDto> getAll(Pageable pageable) {
        final Page<Article> pagedResult = articleRepository.findAll(pageable);
        return pagedResult.map(articleMapper::toArticleDto);
    }
}
