package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {

    List<User> listUsers();

    void saveUser(User user);

    User findByUsername(String username);

    User findById(Long id);

    void updateUser(User user);

    void deleteById(Long id);
}
