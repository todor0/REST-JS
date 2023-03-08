package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> listUsers();

    void saveUser(User user);

    User findByUsername(String username);

    User findById(Long id);

    void updateUser(User user);

    void deleteById(Long id);
}
