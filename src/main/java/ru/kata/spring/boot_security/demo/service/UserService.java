package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;

public interface UserService extends UserDetailsService {

    Set<User> allUsers();

    void saveUser(User user);

    User findById(Long id);

    void updateUser(User user);

    void deleteById(Long id);
}
