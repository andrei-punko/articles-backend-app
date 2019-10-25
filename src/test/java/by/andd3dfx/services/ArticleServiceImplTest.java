package by.andd3dfx.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.lenient;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.mappers.ArticleMapper;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.services.exceptions.ArticleNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepositoryMock;

    @Mock
    private ArticleMapper articleMapperMock;

    @Mock
    private Clock clockMock;
    private Clock fixedClock;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @BeforeEach
    public void before() {
        fixedClock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.systemDefault());
        // Allow unnecessary stubbing:
        lenient().doReturn(fixedClock.instant()).when(clockMock).instant();
        lenient().doReturn(fixedClock.getZone()).when(clockMock).getZone();
    }

    @Test
    void create() {
        ArticleDto articleDto = new ArticleDto();
        Article article = new Article();
        Article updatedArticle = new Article();
        ArticleDto updatedArticleDto = new ArticleDto();

        Mockito.when(articleMapperMock.toArticle(articleDto)).thenReturn(article);
        Mockito.when(articleRepositoryMock.save(article)).thenReturn(updatedArticle);
        Mockito.when(articleMapperMock.toArticleDto(updatedArticle)).thenReturn(updatedArticleDto);

        ArticleDto result = articleService.create(articleDto);

        Mockito.verify(articleMapperMock).toArticle(articleDto);
        Mockito.verify(articleRepositoryMock).save(article);
        Mockito.verify(articleMapperMock).toArticleDto(updatedArticle);
        assertThat(result, is(updatedArticleDto));
        assertThat(articleDto.getDateCreated(), is(LocalDateTime.now(fixedClock)));
        assertThat(articleDto.getDateUpdated(), is(LocalDateTime.now(fixedClock)));
    }

    @Test
    void update() {
        final Long ARTICLE_ID = 123L;
        Article article = new Article();
        Article savedArticle = new Article();
        Optional<Article> optionalArticle = Optional.of(article);
        ArticleDto updatedArticleDto = new ArticleDto();
        updatedArticleDto.setTitle("New title");

        Mockito.when(articleRepositoryMock.findById(ARTICLE_ID)).thenReturn(optionalArticle);
        Mockito.when(articleRepositoryMock.save(article)).thenReturn(savedArticle);
        Mockito.when(articleMapperMock.toArticleDto(savedArticle)).thenReturn(updatedArticleDto);

        articleService.update(ARTICLE_ID, updatedArticleDto);

        Mockito.verify(articleRepositoryMock).findById(ARTICLE_ID);
        Mockito.verify(articleMapperMock).toArticle(updatedArticleDto, article);
        Mockito.verify(articleRepositoryMock).save(article);
        Mockito.verify(articleMapperMock).toArticleDto(savedArticle);
        assertThat(article.getDateUpdated(), is(LocalDateTime.now(fixedClock)));
    }

    @Test
    void updateAbsentArticle() {
        final Long ARTICLE_ID = 123L;
        Optional<Article> optionalArticle = Optional.empty();
        Mockito.when(articleRepositoryMock.findById(ARTICLE_ID)).thenReturn(optionalArticle);
        ArticleDto updatedArticleDto = new ArticleDto();

        try {
            articleService.update(ARTICLE_ID, updatedArticleDto);

            fail("Exception should be thrown");
        } catch (ArticleNotFoundException ex) {
            Mockito.verify(articleRepositoryMock).findById(ARTICLE_ID);
        }
    }

    @Test
    void delete() {
        final Long ARTICLE_ID = 123L;

        articleService.delete(ARTICLE_ID);

        Mockito.verify(articleRepositoryMock).deleteById(ARTICLE_ID);
    }

    @Test
    void deleteAbsentArticle() {
        final Long ARTICLE_ID = 123L;
        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(articleRepositoryMock).deleteById(ARTICLE_ID);

        try {
            articleService.delete(ARTICLE_ID);

            fail("Exception should be thrown");
        } catch (ArticleNotFoundException ex) {
            Mockito.verify(articleRepositoryMock).deleteById(ARTICLE_ID);
            assertThat("Wrong message", ex.getMessage(), is("Could not find an article by id=" + ARTICLE_ID));
        }
    }

    @Test
    void getAll() {
        List<Article> articles = new ArrayList<>();
        Mockito.when(articleRepositoryMock.findAllByOrderByTitle()).thenReturn(articles);
        List<ArticleDto> articleDtos = new ArrayList<>();
        Mockito.when(articleMapperMock.toArticleDtos(articles)).thenReturn(articleDtos);

        List<ArticleDto> result = articleService.getAll();

        Mockito.verify(articleRepositoryMock).findAllByOrderByTitle();
        Mockito.verify(articleMapperMock).toArticleDtos(articles);
        assertThat(result, is(articleDtos));
    }
}
