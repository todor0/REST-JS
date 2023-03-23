package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RestAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<Set<User>> getUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User with id = " + id + " deleted");
    }

}
