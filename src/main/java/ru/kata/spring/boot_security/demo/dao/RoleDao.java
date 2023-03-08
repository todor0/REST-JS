package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDao {

    List<Role> listRoles();

    void save(Role role);

    Role findRoleById(Long id);

    Role findRoleByName(String name);
}
