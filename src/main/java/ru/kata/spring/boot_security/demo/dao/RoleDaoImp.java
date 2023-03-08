package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImp implements RoleDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> listRoles() {
        return entityManager.createQuery("from Role ", Role.class).getResultList();
    }

    @Override
    public void save(Role role) {
        if (!(listRoles().contains(role))) {
            entityManager.persist(role);
        }
    }

    @Override
    public Role findRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role findRoleByName(String name) {
        return entityManager.createQuery(
                "select r from Role r where r.name = :name", Role.class)
                .setParameter("name", name).getSingleResult();
    }
}
