package integration.testing.spring.webflux;

import integration.testing.spring.webflux.dao.UserRepository;
import integration.testing.spring.webflux.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerTest {
    @MockBean
    private UserRepository repository;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testCreateUser() {
        User user = new User();
        user.setId(1);
        user.setName("John Smith");
        user.setInitialAmount(1000);

        Mockito.when(repository.save(user)).thenReturn(Mono.just(user));

        webClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(user))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(repository, times(1)).save(user);
    }

    @Test
    void testGetUsersByName() {
        User user = new User();
        user.setId(1);
        user.setName("John Smith");
        user.setInitialAmount(1000);

        List<User> list = new ArrayList<>();
        list.add(user);

        Flux<User> userFlux = Flux.fromIterable(list);

        Mockito
                .when(repository.findByName("John Smith"))
                .thenReturn(userFlux);

        webClient.get().uri("/name/{name}", "John Smith")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class);

        Mockito.verify(repository, times(1)).findByName("John Smith");
    }

}
