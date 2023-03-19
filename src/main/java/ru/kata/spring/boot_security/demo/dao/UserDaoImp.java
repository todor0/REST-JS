package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class UserDaoImp implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDaoImp(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Set<User> allUsers() {
        Stream<User> userStream = entityManager.createQuery("from User", User.class).getResultStream();

        return userStream.collect(Collectors.toSet());
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public User findByUsername(String username) {
        return entityManager.createQuery("select u from User u join fetch u.roles" +
                        " where u.username = :username", User.class)
                .setParameter("username", username).getSingleResult();
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        if (user.getPassword().length() == 0) {
            user.setPassword(findById(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        entityManager.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }
}
