package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

public interface RolesRepository extends JpaRepository<Role, Integer> {
    @Query("select r from Role r")
    Set<Role> getSetOfRoles();

}
