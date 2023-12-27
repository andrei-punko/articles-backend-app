package by.andd3dfx.persistence.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import by.andd3dfx.dto.ArticleSearchCriteria;
import by.andd3dfx.dto.DeleteArticleSearchCriteria;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.persistence.entities.Author;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class ArticleRepositoryCustomImplTest {

    @Autowired
    private ArticleRepository repository;

    private Article entity;
    private Article entity2;
    private Article entity3;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        entity = buildArticle("Ivan", "HD", LocalDateTime.parse("2010-12-03T10:15:30"));
        entity2 = buildArticle("Vasily", "HD", LocalDateTime.parse("2011-12-03T10:15:30"));
        entity3 = buildArticle("Ivan", "4K", LocalDateTime.parse("2012-12-03T10:15:30"));
        repository.saveAll(Arrays.asList(entity, entity2, entity3));
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void existsByCriteriaWithoutParams() {
        assertThat(repository.existsByCriteria(new ArticleSearchCriteria()), is(true));
    }

    @Test
    public void existsByCriteriaWithUserId() {
        assertThat("Wrong result for Ivan", repository.existsByCriteria(
            new ArticleSearchCriteria("Ivan", null, null)), is(true));
        assertThat("Wrong result for Vasily", repository.existsByCriteria(
            new ArticleSearchCriteria("Vasily", null, null)), is(true));
        assertThat("Wrong result for unknown", repository.existsByCriteria(
            new ArticleSearchCriteria("unknown", null, null)), is(false));
    }

    @Test
    public void existsByCriteriaWithResolution() {
        assertThat("Wrong result for HD", repository.existsByCriteria(
            new ArticleSearchCriteria(null, null, "HD")), is(true));
        assertThat("Wrong result for 4K", repository.existsByCriteria(
            new ArticleSearchCriteria(null, null, "4K")), is(true));
        assertThat("Wrong result for unknown", repository.existsByCriteria(
            new ArticleSearchCriteria(null, null, "unknown")), is(false));
    }

    @Test
    public void existsByCriteriaWithTimestamp() {
        assertThat("Wrong result for e1-e2 date", repository.existsByCriteria(
            new ArticleSearchCriteria(null, "2011-11-03T10:15:30", null)), is(true));
        assertThat("Wrong result for e2-e3 date", repository.existsByCriteria(
            new ArticleSearchCriteria(null, "2012-11-03T10:15:30", null)), is(true));
        assertThat("Wrong result for future date", repository.existsByCriteria(
            new ArticleSearchCriteria(null, "2013-11-03T10:15:30", null)), is(false));
    }

    @Test
    public void existsByCriteriaWithAllParams() {
        assertThat("Wrong result for valid Ivan subscription", repository.existsByCriteria(
            new ArticleSearchCriteria("Ivan", "2012-11-03T10:15:30", "4K")), is(true));
        assertThat("Wrong result for expired Ivan subscription", repository.existsByCriteria(
            new ArticleSearchCriteria("Ivan", "2012-11-03T10:15:30", "HD")), is(false));
    }

    @Test
    public void findByCriteriaWithoutParams() {
        List<Article> result = repository.findByCriteria(new ArticleSearchCriteria());

        assertThat("Wrong records amount", result.size(), is(3));
        assertThat("Not found some entity", result.containsAll(Arrays.asList(entity, entity2, entity3)), is(true));
    }

    @Test
    public void findByCriteriaWithUserId() {
        List<Article> result = repository.findByCriteria(new ArticleSearchCriteria("Ivan", null, null));

        assertThat("Wrong records amount", result.size(), is(2));
        assertThat("Not found some entity", result.containsAll(Arrays.asList(entity, entity3)), is(true));
    }

    @Test
    public void findByCriteriaWithResolution() {
        List<Article> result = repository.findByCriteria(new ArticleSearchCriteria(null, null, "HD"));

        assertThat("Wrong records amount", result.size(), is(2));
        assertThat("Not found some entity", result.containsAll(Arrays.asList(entity, entity2)), is(true));
    }

    @Test
    public void findByCriteriaWithTimestamp() {
        List<Article> result = repository.findByCriteria(new ArticleSearchCriteria(null, "2011-11-03T10:15:30", null));

        assertThat("Wrong records amount", result.size(), is(2));
        assertThat("Not found some entity", result.containsAll(Arrays.asList(entity2, entity3)), is(true));
    }

    @Test
    public void findByCriteriaWithAllParams() {
        List<Article> result = repository.findByCriteria(
            new ArticleSearchCriteria("Ivan", "2012-11-03T10:15:30", "4K"));

        assertThat("Wrong records amount", result.size(), is(1));
        assertThat("Not found some entity", result.containsAll(Arrays.asList(entity3)), is(true));

        List<Article> result2 = repository.findByCriteria(
            new ArticleSearchCriteria("Ivan", "2012-11-03T10:15:30", "HD"));

        assertThat("Wrong records amount", result2.size(), is(0));
    }

    @Test
    public void deleteByCriteria() {
        final String userId = entity2.getTitle();
        final DeleteArticleSearchCriteria deleteCriteria = new DeleteArticleSearchCriteria(userId);
        final ArticleSearchCriteria searchCriteria = new ArticleSearchCriteria();
        searchCriteria.setTitle(userId);

        repository.deleteByCriteria(deleteCriteria);

        assertTrue(repository.findByCriteria(searchCriteria).isEmpty());
        assertEquals(repository.findByCriteria(new ArticleSearchCriteria()).size(), 2);
    }

    @Test
    public void deleteByCriteriaForEmptyCriteria() {
        repository.deleteByCriteria(new DeleteArticleSearchCriteria());

        assertTrue(repository.findByCriteria(new ArticleSearchCriteria()).isEmpty());
    }

    public static Article buildArticle(String title, String summary, LocalDateTime timestamp) {
        Article article = new Article();
        article.setTitle(title);
        article.setSummary(summary);
        article.setText("any text");
        article.setTimestamp(timestamp);
        article.setAuthor(new Author() {{
            setId(1L);
        }});
        return article;
    }
}