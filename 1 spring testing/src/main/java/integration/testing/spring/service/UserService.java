package integration.testing.spring.service;

import integration.testing.spring.model.User;
import integration.testing.spring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional//(propagation = Propagation.REQUIRES_NEW)
    public void transactionalMethod(User user) {
        userRepository.save(user);
    }
}
