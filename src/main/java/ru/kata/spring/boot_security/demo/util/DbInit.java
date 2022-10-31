package ru.kata.spring.boot_security.demo.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RolesRepository;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DbInit {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public DbInit(UsersRepository usersRepository, PasswordEncoder passwordEncoder, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }


    @PostConstruct
    @Transactional
    public void init() {
        Optional<User> user = usersRepository.findByUsername("admin");
        if (user.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            String encode = passwordEncoder.encode("admin");
            admin.setPassword(encode);
            admin.setRoles(new HashSet<>());

            Role adminRole = rolesRepository.getSetOfRoles().stream().filter(role -> Objects.equals(role.getName(), "ROLE_ADMIN")).toList().get(0);
            admin.getRoles().add(adminRole);

            admin.setAge(12);
            usersRepository.save(admin);
        }

    }
}
