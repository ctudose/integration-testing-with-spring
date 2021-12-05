package integration.testing.spring;

import integration.testing.spring.listeners.DatabaseOperationsListener;
import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static integration.testing.spring.UsersHelper.buildUsersList;
import static integration.testing.spring.UsersHelper.getIterations;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@TestExecutionListeners(value = {
        DatabaseOperationsListener.class,
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class
})
class ListenersTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        TestContextManager testContextManager = new TestContextManager(getClass());
        System.out.println("In ListenersTest, testContextManager.getTestExecutionListeners().size() = " + testContextManager.getTestExecutionListeners().size());
        for (TestExecutionListener listener : testContextManager.getTestExecutionListeners()) {
            System.out.println(listener);
        }
    }

    @Test
    void storeUpdateRetrieve() {
        List<User> users = buildUsersList();
        userRepository.saveAll(users);

        for (User user : users) {
            user.setName("Updated " + user.getName());
        }

        userRepository.saveAll(users);

        assertEquals(getIterations(), userRepository.findAll().size());
    }

}
