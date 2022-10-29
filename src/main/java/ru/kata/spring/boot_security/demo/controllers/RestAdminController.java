package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> index() {
        return new ResponseEntity<>(userService.index(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable("id") int id) {
        return new ResponseEntity<>(userService.show(id).get(), HttpStatus.OK);
    }

    @PostMapping
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
}
