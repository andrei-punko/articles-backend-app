package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import by.andd3dfx.ArticlesBackendAppApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = ArticlesBackendAppApplication.class)
@WebAppConfiguration
class AuthorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext)
            .build();
    }

    @Test
    public void readAuthor() throws Exception {
        mockMvc.perform(get("/api/v1/authors/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("Тихон")))
            .andExpect(jsonPath("$.lastName", is("Задонский")));
    }

    @Test
    public void readAbsentAuthor() throws Exception {
        String message = mockMvc.perform(get("/api/v1/authors/345"))
            .andExpect(status().isNotFound())
            .andReturn().getResolvedException().getMessage();
        assertThat(message, containsString("Could not find an author by id=345"));
    }

    @Test
    public void readAuthors() throws Exception {
        mockMvc.perform(get("/api/v1/authors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(11)));
    }
}
