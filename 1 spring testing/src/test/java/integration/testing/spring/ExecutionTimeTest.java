package integration.testing.spring;

import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static integration.testing.spring.UsersHelper.buildUsersList;
import static integration.testing.spring.UsersHelper.getIterations;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class ExecutionTimeTest {

    @Autowired
    private UserRepository userRepository;

    private static long time1;

    private static long time2;

    @BeforeAll
    static void beforeAll() {
        time1 = System.nanoTime();
    }

    @RepeatedTest(10)
    void storeUpdateRetrieve() {
        List<User> users = buildUsersList();
        userRepository.saveAll(users);

        for (User user : users) {
            user.setName("Updated " + user.getName());
        }

        userRepository.saveAll(users);

        assertEquals(getIterations(), userRepository.findAll().size());

    }

    @AfterAll
    static void afterAll() {
        time2 = System.nanoTime();

        long timeDiff = (time2 - time1) / 1000_000;
        System.out.println("Execution time: " + timeDiff);
    }

}
