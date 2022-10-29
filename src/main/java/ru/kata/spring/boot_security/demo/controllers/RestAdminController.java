package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> index() {
        return new ResponseEntity<>(userService.index(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> show(@PathVariable("id") int id) {
        return new ResponseEntity<>(userService.show(id).get(), HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<HttpStatus> create(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody User user) {
        userService.update(id, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/show")
    public ResponseEntity<User> showUserInfo(Principal principal) {
        System.out.println("В контроллере");
        String name = principal.getName();
        User user = userService.findByUsername(name);
        System.out.println(user);
        return new ResponseEntity<>(userService.findByUsername(name), HttpStatus.OK);
    }
}
