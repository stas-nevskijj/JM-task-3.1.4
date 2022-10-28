package ru.kata.spring.boot_security.demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class UserServiceImpl implements UserService {
    private final RoleDAO roleDAO;
    private final UsersRepository usersRepository;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, RoleDAO roleDAO) {
        this.usersRepository = usersRepository;
        this.roleDAO = roleDAO;
    }

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    public void init() {
        Optional<User> user = usersRepository.findByUsername("admin");
        if (user.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            String encode = passwordEncoder.encode("admin");
            admin.setPassword(encode);
            admin.getRoles().add(new Role("ROLE_ADMIN"));
            admin.setAge(12);
            usersRepository.save(admin);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<User> index() {
        return usersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> show(int id) {
        return usersRepository.findById(id);
    }

    @Override
    @Transactional
    public void save(User person) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("user.json"), person);
        } catch (IOException e) {
            throw new RuntimeException();
        }


        String encode = passwordEncoder.encode(person.getPassword());
        person.setPassword(encode);

        Set<Role> copyOfRoles = new HashSet<>(person.getRoles());
        person.getRoles().clear();
        for (Role personRole : copyOfRoles) {
            personRole.setName("ROLE_" + personRole.getName());

            for (Role mainRole : roleDAO.getAllRoles()) {
                if (Objects.equals(mainRole.getName(), personRole.getName())) {
                    person.getRoles().add(mainRole);
                }
            }
        }


        usersRepository.saveAndFlush(person);
    }

    @Override
    @Transactional
    public void update(int id, User updatedPerson) {
        if (!updatedPerson.getPassword().equals("")) {
            String encode = passwordEncoder.encode(updatedPerson.getPassword());
            updatedPerson.setPassword(encode);
        }


        Set<Role> copyOfRoles = new HashSet<>(updatedPerson.getRoles());
        updatedPerson.getRoles().clear();
        for (Role personRole : copyOfRoles) {
            personRole.setName("ROLE_" + personRole.getName());

            for (Role mainRole : roleDAO.getAllRoles()) {
                if (Objects.equals(mainRole.getName(), personRole.getName())) {
                    updatedPerson.getRoles().add(mainRole);
                }
            }
        }

        User user = usersRepository.findById(id).get();
        user.setUsername(updatedPerson.getUsername());
        user.setAge(updatedPerson.getAge());
        user.setPassword(updatedPerson.getPassword());
        user.setRoles(updatedPerson.getRoles());
        usersRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        usersRepository.deleteById(id);
    }
}
