package integration.testing.spring;

import integration.testing.spring.beans.CsvDataLoader;
import integration.testing.spring.model.Auction;
import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CsvDataLoader.class)
public class Application {

	@Autowired
	private Auction auction;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ApplicationRunner configureRepository(UserRepository userRepository) {
		return args -> {
			for (User user : auction.getUsers()) {
				userRepository.save(user);
			}
		};
	}

}
