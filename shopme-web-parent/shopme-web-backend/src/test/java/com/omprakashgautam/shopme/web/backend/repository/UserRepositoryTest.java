package com.omprakashgautam.shopme.web.backend.repository;

import com.omprakashgautam.shopme.commons.entity.Role;
import com.omprakashgautam.shopme.commons.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

    public static final long NOT_NULL = 0L;
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUserWithOneRole(){
        Role adminRole = testEntityManager.find(Role.class, 1L);
        User user = new User("omprakash201194@gmail.com", "password","Omprakash", "Gautam");
        user.addRole(adminRole);
        User savedUser = repo.save(user);
        Assertions.assertThat(savedUser.getId()).isGreaterThan(NOT_NULL);
    }

    @Test
    public void testCreateUserWithTwoRoles(){
        User userStuart = new User("stuart@gmail.com","password","Stuart","Little");
        Role editorRole = new Role(3L);
        Role assitantRole = new Role(5L);

        userStuart.setRoles(Set.of(editorRole,assitantRole));
        repo.save(userStuart);

        Assertions.assertThat(userStuart.getId()).isGreaterThan(NOT_NULL);
    }

    @Test
    public void testListAllUsers(){
        List<User> listAllUsers = repo.findAll();
        listAllUsers.forEach(System.out::println);
    }

    @Test
    public void testGetUserById(){
        User user = repo.findById(1L).get();
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User user = repo.findById(2L).get();
        user.setEnabled(true);
        user.setEmail("ogautam@qualys.com");

        User savedUser = repo.save(user);
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("ogautam@qualys.com");
    }

    @Test
    public void testUpdateUserRoles(){
        User user = repo.findById(2L).get();
        Role editorRole = new Role(3L);
        Role salesRole = new Role(2L);
        user.getRoles().remove(editorRole);
        user.addRole(salesRole);
        repo.save(user);
    }

    @Test
    public void testDeleteUser(){
        Long userId = 2L;
        repo.deleteById(userId);
    }

    @Test
    public void testUserByGetEmail(){
        String email = "omprakash201194@gmail.com";
        Optional<User> userByEmail = repo.getUserByEmail(email);
        Assertions.assertThat(userByEmail.isPresent()).isTrue();
    }

    @Test
    public void testCountById(){
        Long id = 1L;
        Long count = repo.countById(id);

        Assertions.assertThat(count).isNotNull().isGreaterThan(0L);
    }
}