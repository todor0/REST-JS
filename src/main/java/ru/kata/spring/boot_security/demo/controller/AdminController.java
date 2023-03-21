package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("roles", roleService.allRoles());
        model.addAttribute("user", new User());
        return "admin";
    }

    @PutMapping ("/create")
    public String createUserPage(User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping ("/edit")
    public String editUser(User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/user-delete")
    public String deleteUser(@RequestParam(required = false, value = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
