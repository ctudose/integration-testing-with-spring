package integration.testing.spring;

import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestContextManagerInfoTest {

	@Autowired
	private UserRepository userRepository;

	private TestContextManager testContextManager;

	@BeforeEach
	void beforeEach() {
		testContextManager = new TestContextManager(getClass());

		System.out.println("Listeners size = " + testContextManager.getTestExecutionListeners().size());

		for (TestExecutionListener listener : testContextManager.getTestExecutionListeners()) {
			System.out.println(listener);
		}

	}

	@Test
//	@DirtiesContext
	void saveRetrieve() {
		User user = new User("User1");
		userRepository.save(user);
		List<User> users = userRepository.findAll();

		assertAll(
				() -> assertEquals(1, users.size()),
				() -> assertEquals("User1", users.get(0).getName())
		);
	}

}
