package by.andd3dfx.services.impl;

import by.andd3dfx.annotations.CustomLog;
import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.mappers.ArticleMapper;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.services.IArticleService;
import by.andd3dfx.exceptions.ArticleNotFoundException;
import java.time.Clock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final Clock clock;

    @CustomLog
    @Transactional
    @Override
    public ArticleDto create(ArticleDto articleDto) {
        Article entity = articleMapper.toArticle(articleDto);
        Article savedEntity = articleRepository.save(entity);
        return articleMapper.toArticleDto(savedEntity);
        // TODO: returned author with id only after creation. Need to consider, do we need to return author with all fields populated
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleDto get(Long id) {
        return articleRepository.findById(id)
            .map(articleMapper::toArticleDto)
            .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @CustomLog
    @Transactional
    @Override
    public void update(Long id, ArticleUpdateDto articleUpdateDto) {
        articleRepository.findById(id)
            .map(article -> {
                articleMapper.toArticle(articleUpdateDto, article);
                Article savedArticle = articleRepository.save(article);
                return articleMapper.toArticleDto(savedArticle);
            }).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @CustomLog
    @Transactional
    @Override
    public void delete(Long id) {
        try {
            articleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ArticleNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleDto> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Article> pagedResult = articleRepository.findAll(pageRequest);
        return articleMapper.toArticleDtoList(pagedResult.getContent());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ArticleDto> getAll(Pageable pageable) {
        final Page<Article> pagedResult = articleRepository.findAll(pageable);
        return pagedResult.map(articleMapper::toArticleDto);
    }
}
