package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import by.andd3dfx.ArticlesBackendAppApplication;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = ArticlesBackendAppApplication.class)
@WebAppConfiguration
class AuthorControllerIntegrationTest {

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
    public void readAuthor() throws Exception {
        mockMvc.perform(get("/authors/1")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("Тихон")))
            .andExpect(jsonPath("$.lastName", is("Задонский")));
    }

    @Test
    public void readAbsentAuthor() throws Exception {
        String message = mockMvc.perform(get("/authors/345")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an author by id=345"));
    }

    @Test
    public void readAuthors() throws Exception {
        mockMvc.perform(get("/authors")
            .contentType(CONTENT_TYPE)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(11)))
        ;
    }
}
