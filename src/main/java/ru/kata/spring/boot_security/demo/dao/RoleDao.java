package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.Set;

public interface RoleDao {

    Set<Role> allRoles();

    void save(Role role);

    Role findRoleById(Long id);

    Role findRoleByName(String name);
}
