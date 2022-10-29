package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserDetailService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserDetailService userDetailService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserDetailService userDetailService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDetailService = userDetailService;
    }


    @GetMapping
    public String index(Model model, Principal principal) {
        model.addAttribute("users", userService.index());

        String name = principal.getName();
        User currentUser = userDetailService.loadUserByUsername(name).get();
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("allRoles", roleService.getAllRoles());

        model.addAttribute("newUser", new User());
        return "bootstrap4/admin";
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") int id,
                       Model model) {
        model.addAttribute("user", userService.show(id));
        return "bootstrap4/admin";
    }

}