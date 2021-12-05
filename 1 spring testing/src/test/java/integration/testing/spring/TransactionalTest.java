package integration.testing.spring;

import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import integration.testing.spring.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static integration.testing.spring.UsersHelper.buildUsersList;
import static integration.testing.spring.UsersHelper.getIterations;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TransactionalTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
    }


    @RepeatedTest(2)
    void storeUpdateRetrieve() {
        List<User> users = buildUsersList();
        userRepository.saveAll(users);

        for (User user : users) {
            user.setName("Updated " + user.getName());
        }

        userRepository.saveAll(users);

        assertEquals(getIterations(), userRepository.findAll().size());

        userService.transactionalMethod(users.get(0));

        System.out.println("end of method, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
    }

    @AfterEach
    void afterEach() {
        System.out.println("afterEach, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
    }

}
