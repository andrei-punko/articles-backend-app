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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
        final Long id = 123L;
        final ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();

        articleController.updateArticle(id, articleUpdateDto);

        Mockito.verify(articleServiceMock).update(id, articleUpdateDto);
    }

    @Test
    void updateAbsentArticle() {
        final Long id = 123L;
        final ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        final ArticleNotFoundException articleNotFoundException = new ArticleNotFoundException(id);
        Mockito.doThrow(articleNotFoundException).when(articleServiceMock).update(id, articleUpdateDto);

        try {
            articleController.updateArticle(id, articleUpdateDto);
            fail("Exception should be thrown");
        } catch (ArticleNotFoundException e) {
            Mockito.verify(articleServiceMock).update(id, articleUpdateDto);
            assertThat("Wrong exception", e, is(articleNotFoundException));
        }
    }

    @Test
    void deleteArticle() {
        final Long id = 123L;

        articleController.deleteArticle(id);

        Mockito.verify(articleServiceMock).delete(id);
    }

    @Test
    void deleteAbsentArticle() {
        final Long id = 123L;
        final ArticleNotFoundException articleNotFoundException = new ArticleNotFoundException(id);
        Mockito.doThrow(articleNotFoundException).when(articleServiceMock).delete(id);

        try {
            articleController.deleteArticle(id);
            fail("Exception should be thrown");
        } catch (ArticleNotFoundException e) {
            Mockito.verify(articleServiceMock).delete(id);
            assertThat("Wrong exception", e, is(articleNotFoundException));
        }
    }

    @Test
    void readArticles() {
        final List<ArticleDto> articleDtoList = new ArrayList<>();
        final Integer pageNo = 2;
        final Integer pageSize = 20;
        final String sortBy = "title";
        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("title"));
        final Page<ArticleDto> pagedResult = new PageImpl<>(articleDtoList, pageRequest, articleDtoList.size());
        Mockito.when(articleServiceMock.getAll(pageRequest)).thenReturn(pagedResult);

        List<ArticleDto> result = articleController.readArticles(pageNo, pageSize, sortBy);

        Mockito.verify(articleServiceMock).getAll(pageRequest);
        assertThat("Wrong result", result, is(articleDtoList));
    }

    @Test
    void readArticlesPaged() {
        final List<ArticleDto> articleDtoList = new ArrayList<>();
        final Integer pageNo = 2;
        final Integer pageSize = 20;
        final Sort sort = Sort.by("title");
        final Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
        final Page<ArticleDto> pagedResult = new PageImpl<>(articleDtoList, pageRequest, articleDtoList.size());
        Mockito.when(articleServiceMock.getAll(pageRequest)).thenReturn(pagedResult);

        Page<ArticleDto> result = articleController.readArticlesPaged(pageRequest);

        Mockito.verify(articleServiceMock).getAll(pageRequest);
        assertThat("Wrong result.content", result.getContent(), is(articleDtoList));
        assertThat("Wrong number", result.getNumber(), is(2));
        assertThat("Wrong size", result.getSize(), is(20));
        assertThat("Wrong sort", result.getSort(), is(sort));
    }
}
