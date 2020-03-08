package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.dto.AuthorDto;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
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
        mockMvc = webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "USER")
    public void createArticleUnauthorized() throws Exception {
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
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    public void deleteArticle() throws Exception {
        mockMvc.perform(delete("/articles/1")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteArticleUnauthorized() throws Exception {
        mockMvc.perform(delete("/articles/1")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteAbsentArticle() throws Exception {
        String message = mockMvc.perform(delete("/articles/9999")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an article by id=9999"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void readArticleForAdmin() throws Exception {
        mockMvc.perform(get("/articles/1")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void readArticleForUser() throws Exception {
        mockMvc.perform(get("/articles/2")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void readAbsentArticle() throws Exception {
        String message = mockMvc.perform(get("/articles/345")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an article by id=345"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void readArticlesForAdmin() throws Exception {
        mockMvc.perform(get("/articles")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(10)))
            .andExpect(jsonPath("$.number", is(0)))
            .andExpect(jsonPath("$.size", is(50)))
            .andExpect(jsonPath("$.totalPages", is(1)))
            .andExpect(jsonPath("$.totalElements", is(10)));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void readArticlesForUser() throws Exception {
        mockMvc.perform(get("/articles")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(10)))
            .andExpect(jsonPath("$.number", is(0)))
            .andExpect(jsonPath("$.size", is(50)))
            .andExpect(jsonPath("$.totalPages", is(1)))
            .andExpect(jsonPath("$.totalElements", is(10)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void readArticlesWithPageSizeLimit() throws Exception {
        mockMvc.perform(get("/articles")
            .param("size", "5")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(5)))
            .andExpect(jsonPath("$.number", is(0)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPages", is(2)))
            .andExpect(jsonPath("$.totalElements", is(9)))
        ;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("Some tittle value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateArticleTitleUnauthorized() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("Some tittle value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary("Some summary value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleText() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setText("Some text value");

        mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleMultipleFields() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary("Some summary value");
        articleUpdateDto.setText("Some text value");

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Only one field should be modified at once"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateAbsentArticle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("q");

        String message = mockMvc.perform(patch("/articles/123")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an article by id=123"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleWithEmptyTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle("");

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleWithTooLongTitle() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setTitle(createStringWithLength(101));

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Title length must be between 1 and 100"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleWithTooLongSummary() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setSummary(createStringWithLength(260));

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Summary length shouldn't be greater than 255"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateArticleWithEmptyText() throws Exception {
        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setText("");

        String message = mockMvc.perform(patch("/articles/2")
            .contentType(CONTENT_TYPE)
            .content(json(articleUpdateDto))
        )
            .andExpect(status().isBadRequest())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Text length should be 1 at least"));
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
