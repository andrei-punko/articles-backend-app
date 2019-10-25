package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import by.andd3dfx.ArticlesBackendAppApplication;
import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = ArticlesBackendAppApplication.class)
@WebAppConfiguration
class ArticleControllerIntegrationTest {

    private final MediaType CONTENT_TYPE = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        Charset.forName("utf8"));
    private MockMvc mockMvc;
    private HttpMessageConverter httpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        httpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null", httpMessageConverter);
    }

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createArticle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
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

        final String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Article id shouldn't be present"));
    }

    @Test
    public void createArticleWithoutTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        final String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title should be populated"));
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

        final String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void createArticleWithTooLongTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(createStringWithLength(101));
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void createArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary(createStringWithLength(260));
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Summary length shouldn't be greater than 255"));
    }

    @Test
    public void createArticleWithoutText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text should be populated"));
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

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text length should be 1 at least"));
    }

    @Test
    public void createArticleWithoutAuthor() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Author should be populated"));
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

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Unknown author"));
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

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("DateCreated shouldn't be populated"));
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

        String message = mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("DateUpdated shouldn't be populated"));
    }

    @Test
    public void deleteArticle() throws Exception {
        mockMvc.perform(delete("/articles/1")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAbsentArticle() throws Exception {
        String message = mockMvc.perform(delete("/articles/9999")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an article by id=9999"));
    }

    @Test
    public void readArticles() throws Exception {
        mockMvc.perform(get("/articles")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(2)))
            .andExpect(jsonPath("$[1].id", is(3)));
    }

    @Test
    public void updateArticleTitle() throws Exception {
        Article article = articleRepository.findById(2L).get();

        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void updateArticleSummary() throws Exception {
        Article article = articleRepository.findById(2L).get();

        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary("Some summary value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void updateArticleText() throws Exception {
        Article article = articleRepository.findById(2L).get();

        ArticleDto articleDto = new ArticleDto();
        articleDto.setText("Some text value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    public void updateArticleMultipleFields() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(2L);
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text value");

        String message = mockMvc.perform(patch("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Only one field should be modified at once"));
    }

    @Test
    public void updateAbsentArticle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("q");

        String message = mockMvc.perform(patch("/articles/123")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an article by id=123"));
    }

    @Test
    public void updateArticleWithEmptyTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("");

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void updateArticleWithTooLongTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(createStringWithLength(101));

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    public void updateArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary(createStringWithLength(260));

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Summary length shouldn't be greater than 255"));
    }

    @Test
    public void updateArticleWithEmptyText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setText("");

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text length should be 1 at least"));
    }

    @Test
    public void updateArticleWithAuthorPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setAuthor(new AuthorDto());

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Author shouldn't be present"));
    }

    @Test
    public void updateArticleWithDateCreatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setDateCreated(LocalDateTime.now());

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("DateCreated shouldn't be populated"));
    }

    @Test
    public void updateArticleWithDateUpdatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setDateUpdated(LocalDateTime.now());

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("DateUpdated shouldn't be populated"));
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    private String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < length; index++) {
            builder.append("a");
        }
        return builder.toString();
    }
}
