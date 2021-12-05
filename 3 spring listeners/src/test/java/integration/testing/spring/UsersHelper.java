package integration.testing.spring;

import integration.testing.spring.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UsersHelper {

    static int getIterations() {
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            return Integer.parseInt(prop.getProperty("iterations"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static List<User> buildUsersList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < getIterations(); i++) {
            User user = new User("User" + i);
            users.add(user);
        }
        return users;
    }
}
