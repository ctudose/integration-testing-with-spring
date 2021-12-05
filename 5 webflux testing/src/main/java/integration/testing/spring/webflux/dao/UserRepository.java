package integration.testing.spring.webflux.dao;

import integration.testing.spring.webflux.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveMongoRepository<User, Integer> {
    @Query("{ 'name': ?0 }")
    Flux<User> findByName(final String name);
}