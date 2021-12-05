package integration.testing.spring;

import integration.testing.spring.model.Log;
import integration.testing.spring.model.User;
import integration.testing.spring.repositories.LogRepository;
import integration.testing.spring.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import static integration.testing.spring.UsersHelper.buildUsersList;
import static integration.testing.spring.UsersHelper.getIterations;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TransactionsManagementTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @BeforeTransaction
    void beforeTransaction() {
        Assumptions.assumeFalse(TransactionSynchronizationManager.isActualTransactionActive());
//        logRepository.save(new Log("@BeforeTransaction"));
    }


    @RepeatedTest(2)
//    @Rollback(value = false)
//    @Commit
    void storeUpdateRetrieve() {
        Assumptions.assumeTrue(TransactionSynchronizationManager.isActualTransactionActive());

        List<User> users = buildUsersList();
        userRepository.saveAll(users);

        for (User user : users) {
            user.setName("Updated " + user.getName());
        }

        userRepository.saveAll(users);

        assertEquals(getIterations(), userRepository.findAll().size());

    }

    @AfterTransaction
    void afterTransaction() {
        Assumptions.assumeFalse(TransactionSynchronizationManager.isActualTransactionActive());
//        logRepository.save(new Log("@AfterTransaction"));
    }

}
