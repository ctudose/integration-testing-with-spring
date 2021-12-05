package integration.testing.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import integration.testing.spring.beans.CsvDataLoader;
import integration.testing.spring.exceptions.UserNotFoundException;
import integration.testing.spring.model.Auction;
import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(CsvDataLoader.class)
public class RestApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Auction auction;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(new ArrayList<>(auction.getUsers()));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(20)));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUserNotFound() {
        Throwable throwable = assertThrows(NestedServletException.class,
                () -> mvc.perform(
                        get("/users/30"))
                        .andExpect(status().isNotFound()));
        assertEquals(UserNotFoundException.class, throwable.getCause().getClass());
    }

    @Test
    void testPostUser() throws Exception {

        User user = new User("Peter Michelsen");
        user.setRegistered(false);
        when(userRepository.save(user)).thenReturn(user);

        mvc.perform(post("/users")
                .content(new ObjectMapper().writeValueAsString(user))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Peter Michelsen")))
                .andExpect(jsonPath("$.registered", is(Boolean.FALSE)));

        verify(userRepository, times(1)).save(user);

    }

    @Test
    void testPatchUser() throws Exception {
        User user = new User("Sophia Graham");
        user.setRegistered(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        String updates =
                "{\"name\":\"Sophia Jones\", \"registered\":\"true\"}";

        mvc.perform(patch("/users/1")
                .content(updates)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

}
