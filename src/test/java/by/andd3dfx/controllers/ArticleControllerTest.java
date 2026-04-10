package by.andd3dfx.controllers;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import by.andd3dfx.ArticlesBackendAppApplication;
import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.dto.AuthorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = ArticlesBackendAppApplication.class)
@WebAppConfiguration
class ArticleControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    public void createArticle() throws Exception {
        var articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        mockMvc.perform(post("/api/v1/articles")
                        .contentType(APPLICATION_JSON)
                        .content(json(articleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.summary", is(articleDto.getSummary())))
                .andExpect(jsonPath("$.text", is(articleDto.getText())))
                .andExpect(jsonPath("$.author.id", is(1)))
                .andExpect(jsonPath("$.dateCreated", notNullValue()))
                .andExpect(jsonPath("$.dateUpdated", notNullValue()));
    }

    @Test
    public void createArticleWithIdPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Article id shouldn't be present");
    }

    @Test
    public void createArticleWithoutTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Title should be populated");
    }

    @Test
    public void createArticleWithEmptyTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Title length must be between 1 and 100");
    }

    @Test
    public void createArticleWithTooLongTitle() throws Exception {
        var articleDto = new ArticleDto();
        articleDto.setTitle(random(101));
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Title length must be between 1 and 100");
    }

    @Test
    public void createArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary(random(260));
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Summary length shouldn't be greater than 255");
    }

    @Test
    public void createArticleWithoutText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Text should be populated");
    }

    @Test
    public void createArticleWithEmptyText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Text length should be 1 at least");
    }

    @Test
    public void createArticleWithoutAuthor() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Author should be populated");
    }

    @Test
    public void createArticleWithWrongAuthor() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(100L);
        articleDto.setAuthor(authorDto);

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "Unknown author");
    }

    @Test
    public void createArticleWithDateCreatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);
        articleDto.setDateCreated(LocalDateTime.now());

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "DateCreated shouldn't be populated");
    }

    @Test
    public void createArticleWithDateUpdatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);
        articleDto.setDateUpdated(LocalDateTime.now());

        expectValidationFailedWithMessage(
            mockMvc.perform(post("/api/v1/articles")
                .contentType(APPLICATION_JSON)
                .content(json(articleDto))),
            "DateUpdated shouldn't be populated");
    }

    @Test
    public void deleteArticle() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAbsentArticle() throws Exception {
        expectNotFound(
            mockMvc.perform(delete("/api/v1/articles/9999")
                .contentType(APPLICATION_JSON)),
            "Could not find an article by id=9999");
    }

    @Test
    public void readArticle() throws Exception {
        mockMvc.perform(get("/api/v1/articles/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void readAbsentArticle() throws Exception {
        expectNotFound(
            mockMvc.perform(get("/api/v1/articles/345")
                .contentType(APPLICATION_JSON)),
            "Could not find an article by id=345");
    }

    @Test
    public void readArticles() throws Exception {
        mockMvc.perform(get("/api/v1/articles")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.size", is(50)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(10)));
    }

    @Test
    public void readArticlesWithPageSizeLimit() throws Exception {
        mockMvc.perform(get("/api/v1/articles")
                        .param("size", "5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(9)));
    }

    @Test
    public void updateArticleTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("Some tittle value");

        mockMvc.perform(patch("/api/v1/articles/2")
                        .contentType(APPLICATION_JSON)
                        .content(json(articleUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateArticleSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary("Some summary value");

        mockMvc.perform(patch("/api/v1/articles/2")
                        .contentType(APPLICATION_JSON)
                        .content(json(articleUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateArticleText() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setText("Some text value");

        mockMvc.perform(patch("/api/v1/articles/2")
                        .contentType(APPLICATION_JSON)
                        .content(json(articleUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateArticleMultipleFields() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary("Some summary value");
        articleUpdateDto.setText("Some text value");

        expectValidationFailedWithMessage(
            mockMvc.perform(patch("/api/v1/articles/2")
                .contentType(APPLICATION_JSON)
                .content(json(articleUpdateDto))),
            "Only one field should be modified at once");
    }

    @Test
    public void updateAbsentArticle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("q");

        expectNotFound(
            mockMvc.perform(patch("/api/v1/articles/123")
                .contentType(APPLICATION_JSON)
                .content(json(articleUpdateDto))),
            "Could not find an article by id=123");
    }

    @Test
    public void updateArticleWithEmptyTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("");

        expectValidationFailedWithMessage(
            mockMvc.perform(patch("/api/v1/articles/2")
                .contentType(APPLICATION_JSON)
                .content(json(articleUpdateDto))),
            "Title length must be between 1 and 100");
    }

    @Test
    public void updateArticleWithTooLongTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle(random(101));

        expectValidationFailedWithMessage(
            mockMvc.perform(patch("/api/v1/articles/2")
                .contentType(APPLICATION_JSON)
                .content(json(articleUpdateDto))),
            "Title length must be between 1 and 100");
    }

    @Test
    public void updateArticleWithTooLongSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary(random(260));

        expectValidationFailedWithMessage(
            mockMvc.perform(patch("/api/v1/articles/2")
                .contentType(APPLICATION_JSON)
                .content(json(articleUpdateDto))),
            "Summary length shouldn't be greater than 255");
    }

    @Test
    public void updateArticleWithEmptyText() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setText("");

        expectValidationFailedWithMessage(
            mockMvc.perform(patch("/api/v1/articles/2")
                .contentType(APPLICATION_JSON)
                .content(json(articleUpdateDto))),
            "Text length should be 1 at least");
    }

    private void expectValidationFailedWithMessage(ResultActions actions, String messageSubstring) throws Exception {
        actions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)))
            .andExpect(jsonPath("$.error", is("Validation Failed")))
            .andExpect(jsonPath("$.violations[*].message", hasItem(containsString(messageSubstring))));
    }

    private void expectNotFound(ResultActions actions, String messageSubstring) throws Exception {
        actions
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(404)))
            .andExpect(jsonPath("$.error", is("Not Found")))
            .andExpect(jsonPath("$.message", containsString(messageSubstring)));
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}
