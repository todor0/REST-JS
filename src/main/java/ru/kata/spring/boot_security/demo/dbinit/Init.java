package ru.kata.spring.boot_security.demo.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void addRolesAndUsers() {
        if (roleService.allRoles().size() == 0) {
            roleService.save(new Role("ROLE_ADMIN"));
            roleService.save(new Role("ROLE_USER"));
        }

        if (userService.allUsers().size() == 0) {
            User admin = new User("admin", "admin","admin@admin.admin");
            admin.setRoles(roleService.allRoles());
            userService.saveUser(admin);

            User user = new User("user", "user","user@user.user");
            user.setRoles(roleService.allRoles().stream()
                    .filter(role -> Objects.equals(role.getName(), "ROLE_USER"))
                    .collect(Collectors.toSet()));
            userService.saveUser(user);
        }
    }
}
