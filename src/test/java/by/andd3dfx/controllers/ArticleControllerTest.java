package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.services.ArticleService;
import by.andd3dfx.services.exceptions.ArticleNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleServiceMock;

    @InjectMocks
    private ArticleController articleController; // constructor injection

    @Test
    void createArticle() {
        ArticleDto newArticleDto = new ArticleDto();
        ArticleDto resultArticleDto = new ArticleDto();
        Mockito.when(articleServiceMock.create(newArticleDto)).thenReturn(resultArticleDto);

        ArticleDto result = articleController.createArticle(newArticleDto);

        Mockito.verify(articleServiceMock).create(newArticleDto);
        assertThat("Wrong result", result, is(resultArticleDto));
    }

    @Test
    public void readArticle() {
        final Long id = 123L;
        final ArticleDto resultArticleDto = new ArticleDto();
        Mockito.when(articleServiceMock.get(id)).thenReturn(resultArticleDto);

        ArticleDto result = articleController.readArticle(id);

        Mockito.verify(articleServiceMock).get(id);
        assertThat("Wrong result", result, is(resultArticleDto));
    }

    @Test
    public void readAbsentArticle() {
        final Long id = 123L;
        final ArticleNotFoundException articleNotFoundException = new ArticleNotFoundException(id);
        Mockito.when(articleServiceMock.get(id)).thenThrow(articleNotFoundException);

        try {
            articleController.readArticle(id);
            fail("Exception should be thrown");
        } catch (ArticleNotFoundException e) {
            Mockito.verify(articleServiceMock).get(id);
            assertThat("Wrong exception", e, is(articleNotFoundException));
        }
    }

    @Test
    void updateArticle() {
        Long id = 123L;
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();

        articleController.updateArticle(id, articleUpdateDto);

        Mockito.verify(articleServiceMock).update(id, articleUpdateDto);
    }

    @Test
    void deleteArticle() {
        Long id = 123L;

        articleController.deleteArticle(id);

        Mockito.verify(articleServiceMock).delete(id);
    }

    @Test
    void readArticles() {
        List<ArticleDto> articleDtos = new ArrayList<>();
        Mockito.when(articleServiceMock.getAll()).thenReturn(articleDtos);

        List<ArticleDto> result = articleController.readArticles();

        Mockito.verify(articleServiceMock).getAll();
        assertThat("Wrong result", result, is(articleDtos));
    }
}
