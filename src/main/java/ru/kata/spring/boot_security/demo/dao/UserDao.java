package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;

public interface UserDao {

    Set<User> allUsers();

    void saveUser(User user);

    User findByUsername(String username);

    User findById(Long id);

    void updateUser(User user);

    void deleteById(Long id);
}
