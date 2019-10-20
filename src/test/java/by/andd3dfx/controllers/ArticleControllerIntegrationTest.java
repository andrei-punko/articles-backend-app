package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import by.andd3dfx.ArticlesBackendAppApplication;
import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.mappers.ArticleMapper;
import by.andd3dfx.persistence.dao.ArticleRepository;
import by.andd3dfx.persistence.entities.Article;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithoutTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
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

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithTooLongTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        articleDto.setText("Some text");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithoutText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        articleDto.setAuthor(authorDto);

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
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

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void createArticleWithoutAuthor() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
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

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
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
        articleDto.setDateCreated(new Date());

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
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
        articleDto.setDateUpdated(new Date());

        mockMvc.perform(post("/articles")
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
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
        mockMvc.perform(delete("/articles/9999")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNotFound());
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
    public void updateArticle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(2L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
            .andExpect(jsonPath("$.summary", is(articleDto.getSummary())))
            .andExpect(jsonPath("$.text", is(articleDto.getText())))
            .andExpect(jsonPath("$.author.id", notNullValue()))
            .andExpect(jsonPath("$.dateCreated", notNullValue()))
            .andExpect(jsonPath("$.dateUpdated", notNullValue()));
    }

    @Test
    public void updateAbsentArticle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("q");
        articleDto.setSummary("w");
        articleDto.setText("e");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateArticleWithoutId() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithEmptyTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(2L);
        articleDto.setTitle("");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithTooLongTitle() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(2L);
        articleDto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithTooLongSummary() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(2L);
        articleDto.setTitle("Some title");
        articleDto.setSummary("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        articleDto.setText("Some text");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithEmptyText() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(2L);
        articleDto.setTitle("Some title");
        articleDto.setSummary("Some summary value");
        articleDto.setText("");

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithAuthorPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setAuthor(new AuthorDto());

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithDateCreatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setDateCreated(new Date());

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateArticleWithDateUpdatedPopulated() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(123L);
        articleDto.setTitle("Some tittle value");
        articleDto.setSummary("Some summary value");
        articleDto.setText("Some text");
        articleDto.setDateUpdated(new Date());

        mockMvc.perform(put("/articles/" + articleDto.getId())
            .contentType(CONTENT_TYPE)
            .content(json(articleDto))
        )
            .andExpect(status().isBadRequest());
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
