package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenFindByNonExistingEmail_thenReturnEmpty() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexisting@example.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @Sql("classpath:test-users.sql")
    public void whenExistsByEmail_withExistingEmail_thenReturnTrue() {
        // When
        boolean exists = userRepository.existsByEmail("existing@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByEmail_withNonExistingEmail_thenReturnFalse() {
        // When
        boolean exists = userRepository.existsByEmail("nonexisting@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    public void whenSaveUser_thenUserIsPersisted() {
        // Given
        User user = new User();
        user.setEmail("new@example.com");
        user.setPassword("password");
        user.setFirstName("Jane");
        user.setLastName("Doe");

        // When
        User saved = userRepository.save(user);

        // Then
        assertThat(entityManager.find(User.class, saved.getId())).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    public void whenDeleteUser_thenUserIsRemoved() {
        // Given
        User user = new User();
        user.setEmail("delete@example.com");
        user.setPassword("password");
        user = entityManager.persistAndFlush(user);

        // When
        userRepository.delete(user);

        // Then
        assertThat(entityManager.find(User.class, user.getId())).isNull();
    }

    @Test
    public void whenUpdateUser_thenChangesArePersisted() {
        // Given
        User user = new User();
        user.setEmail("update@example.com");
        user.setPassword("password");
        user = entityManager.persistAndFlush(user);

        // When
        user.setFirstName("Updated");
        userRepository.save(user);

        // Then
        User updated = entityManager.find(User.class, user.getId());
        assertThat(updated.getFirstName()).isEqualTo("Updated");
    }
}