package ru.kata.spring.boot_security.demo.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Init {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void addRolesAndUsers() {
        if (roleService.listRoles().size() == 0) {
            roleService.save(new Role("ROLE_ADMIN"));
            roleService.save(new Role("ROLE_USER"));
        }

        if (userService.listUsers().size() == 0) {
            User admin = new User("admin", "admin","admin@admin.admin");
            admin.setRoles(roleService.listRoles());
            userService.saveUser(admin);

            User user = new User("user", "user","user@user.user");
            user.setRoles(roleService.listRoles().stream()
                            .filter(role -> Objects.equals(role.getName(), "ROLE_USER"))
                            .collect(Collectors.toList()));
            userService.saveUser(user);
        }
    }
}
