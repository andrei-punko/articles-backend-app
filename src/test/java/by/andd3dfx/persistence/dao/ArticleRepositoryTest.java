package by.andd3dfx.persistence.dao;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.ArticlesBackendAppApplication;
import by.andd3dfx.persistence.entities.Article;
import by.andd3dfx.persistence.entities.Author;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ArticlesBackendAppApplication.class)
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setup() {
        articleRepository.deleteAll();
        articleRepository.save(buildArticle("Some title", "Some text"));
        articleRepository.save(buildArticle("Some title 2", "Some text 2"));
    }

    @Test
    void findAllByOrderByTitle() {
        List<Article> result = articleRepository.findAllByOrderByTitle();

        assertThat(result.size(), is(2));
        List<String> titles = result.stream().map(Article::getTitle).collect(Collectors.toList());
        assertThat(titles, hasItems("Some title", "Some title 2"));
    }

    private Article buildArticle(String title, String text) {
        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        Author author = new Author();
        author.setId(1L);
        article.setAuthor(author);
        article.setDateCreated(new Date());
        article.setDateUpdated(new Date());
        return article;
    }
}
