package com.mylibrary.user.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * An example of the WebContext aware MVC test for {@link UserController}.
 * Spring context will be loaded and all real Bean implementations will be used.
 * Follow an example {@link UserControllerStandaloneTest#getBookByISBN_NOT_FOUND()} to use mocks.
 *
 * @author Vadym Lotar
 * @see UserController
 * @see SpringBootTest
 * @see MockMvc
 * @see MockMvcBuilders
 * @see UserControllerStandaloneTest
 * @since 0.1.0.RELEASE
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class UserControllerWebContextTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void getBookByISBN() throws Exception {
        createBook();

        mockMvc.perform(get("/library/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"));
    }

    @Test
    public void getBookByISBN_NOT_FOUND() throws Exception {
        mockMvc.perform(get("/library/books/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/problem+json"));
    }

    @Test
    public void getBooks() throws Exception {
        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"));
    }

    @Test
    public void createBook() throws Exception {
        mockMvc.perform(post("/library/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"userName\":\"Rishi Kapoor\",\"idProof\":\"DLIND39384948393939\", \"idType\":\"DL\", \"mobile\":\"9876543210\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"));
    }

}
