package by.andd3dfx.mappers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.persistence.entities.Author;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ArticleMapperTest {

    private ArticleMapper mapper = Mappers.getMapper(ArticleMapper.class);

    @Test
    void toArticleDto() {
        Article article = buildArticle();

        ArticleDto articleDto = mapper.toArticleDto(article);
        checkCompareAssertions(articleDto, article);
    }

    @Test
    void toArticleDtoForNull() {
        assertThat(mapper.toArticleDto(null), nullValue());
    }

    @Test
    void toArticleDtos() {
        Article article = buildArticle();
        List<Article> articles = Arrays.asList(article);

        List<ArticleDto> articleDtos = mapper.toArticleDtos(articles);
        assertThat("Wrong result list size", articleDtos.size(), is(1));
        checkCompareAssertions(articleDtos.get(0), article);
    }

    @Test
    void toArticleDtosForNull() {
        assertThat(mapper.toArticleDtos(null), nullValue());
    }

    @Test
    void toArticle() {
        ArticleDto articleDto = buildArticleDto();

        Article article = mapper.toArticle(articleDto);
        checkCompareAssertions(articleDto, article);
    }

    @Test
    void toArticleForNull() {
        assertThat(mapper.toArticle(null), nullValue());
    }

    @Test
    void toArticleWithTarget() {
        ArticleUpdateDto source = new ArticleUpdateDto();
        final String NEW_TITLE = "New title";
        source.setTitle(NEW_TITLE);
        Article target = buildArticle();
        final String OLD_TEXT = target.getText();

        mapper.toArticle(source, target);

        assertThat(target.getTitle(), is(NEW_TITLE));
        assertThat(target.getText(), is(OLD_TEXT));
    }

    @Test
    void toArticleWithTargetForNull() {
        Article target = buildArticle();
        final String OLD_TEXT = target.getText();

        mapper.toArticle(null, target);

        assertThat(target.getText(), is(OLD_TEXT));
    }

    private Article buildArticle() {
        Article article = new Article();
        article.setId(123L);
        article.setTitle("Some tittle value");
        article.setSummary("Some summary value");
        article.setText("Some text");
        Author author = new Author();
        author.setId(321L);
        author.setFirstName("John");
        author.setLastName("Deer");
        article.setAuthor(author);
        article.setDateCreated(LocalDateTime.of(1980, 9, 21, 0, 0));
        article.setDateUpdated(LocalDateTime.of(2011, 3, 5, 0, 0));
        return article;
    }

    private ArticleDto buildArticleDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(321L);
        authorDto.setFirstName("John");
        authorDto.setLastName("Deer");
        articleDto.setAuthor(authorDto);
        articleDto.setDateCreated(LocalDateTime.of(1980, 9, 21, 0, 0));
        articleDto.setDateUpdated(LocalDateTime.of(2011, 3, 5, 0, 0));
        return articleDto;
    }

    private void checkCompareAssertions(ArticleDto articleDto, Article article) {
        assertThat("Wrong id", article.getId(), is(articleDto.getId()));
        assertThat("Wrong title", article.getTitle(), is(articleDto.getTitle()));
        assertThat("Wrong summary", article.getSummary(), is(articleDto.getSummary()));
        assertThat("Wrong text", article.getText(), is(articleDto.getText()));
        assertThat("Wrong author id", article.getAuthor().getId(), is(articleDto.getAuthor().getId()));
        assertThat("Wrong author first name", article.getAuthor().getFirstName(),
            is(articleDto.getAuthor().getFirstName()));
        assertThat("Wrong author last name", article.getAuthor().getLastName(),
            is(articleDto.getAuthor().getLastName()));
        assertThat("Wrong date created", article.getDateCreated(), is(articleDto.getDateCreated()));
        assertThat("Wrong date updated", article.getDateUpdated(), is(articleDto.getDateUpdated()));
    }
}