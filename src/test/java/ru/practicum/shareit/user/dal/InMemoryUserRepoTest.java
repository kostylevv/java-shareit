package ru.practicum.shareit.user.dal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

class InMemoryUserRepoTest {
    UserRepository userRepository;
    User user1, user2, user3;

    @BeforeEach
    void beforeEach() {
        userRepository = new InMemoryUserRepo();
        user1 = User.builder().name("Name 1").email("n1@e.com").build();
        user2 = User.builder().name("Name 2").email("n2@e.com").build();
        user3 = User.builder().name("Name 3").email("n3@e.com").build();
    }


    @Test
    void getUserById() {
        long uid1 = userRepository.addUser(user1).getId();
        long uid2 = userRepository.addUser(user2).getId();

        Assertions.assertEquals(user1.getEmail(), userRepository.getUserById(uid1).get().getEmail());
        Assertions.assertEquals(user2.getEmail(), userRepository.getUserById(uid2).get().getEmail());
        Assertions.assertTrue(userRepository.getUserById(3).isEmpty());
    }

    @Test
    void getUserByEmail() {
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        Assertions.assertTrue(userRepository.getUserByEmail("n2@e.com").isPresent());
        Assertions.assertTrue(userRepository.getUserByEmail("n3@e.com").isEmpty());
    }

    @Test
    void getAllUsers() {
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        userRepository.addUser(user3);
        Assertions.assertEquals(3, userRepository.getAllUsers().size());
    }

    @Test
    void addUser() {
        User added = userRepository.addUser(user1);
        Assertions.assertEquals(added.getEmail().trim().toUpperCase(), user1.getEmail().trim().toUpperCase());
        Assertions.assertEquals(added.getName(), user1.getName());
    }

    @Test
    void addUserBlankEmail() {
        User blank = User.builder().name("Name 1").build();
        Assertions.assertThrows(ValidationException.class, () -> userRepository.addUser(blank));
    }

    @Test
    void addUserDuplicateEmail() {
        userRepository.addUser(user1);
        User duplicate = User.builder().email(user1.getEmail()).build();
        Assertions.assertThrows(DuplicatedDataException.class, () -> userRepository.addUser(duplicate));
    }

    @Test
    void updateUser() {
        User added = userRepository.addUser(user1);
        User updated = User.builder().id(added.getId()).email("upd@eml.com").name(added.getName()).build();
        User requestedUpdated = userRepository.updateUser(updated);
        Assertions.assertEquals(requestedUpdated.getEmail().trim().toUpperCase(), updated.getEmail().trim().toUpperCase());
        Assertions.assertEquals(requestedUpdated.getName(), added.getName());
        Assertions.assertEquals(requestedUpdated.getId(), added.getId());
    }

    @Test
    void updateUserBlankEmail() {
        User added = userRepository.addUser(user1);
        User updated = User.builder().id(added.getId()).email("").name(added.getName()).build();
        Assertions.assertThrows(ValidationException.class, () -> userRepository.updateUser(updated));
    }

    @Test
    void updateUserDuplicateEmail() {
        User added = userRepository.addUser(user1);
        userRepository.addUser(user2);
        userRepository.addUser(user3);
        User updated = User.builder().id(added.getId()).email(user2.getEmail()).name(added.getName()).build();
        Assertions.assertThrows(DuplicatedDataException.class, () -> userRepository.updateUser(updated));
    }

    @Test
    void deleteUser() {
        Assertions.assertEquals(0, userRepository.getAllUsers().size());
        long uid1 = userRepository.addUser(user1).getId();
        long uid2 = userRepository.addUser(user2).getId();
        long uid3 = userRepository.addUser(user3).getId();
        Assertions.assertEquals(3, userRepository.getAllUsers().size());
        userRepository.deleteUser(uid1);
        Assertions.assertEquals(2, userRepository.getAllUsers().size());
        userRepository.deleteUser(uid2);
        Assertions.assertEquals(1, userRepository.getAllUsers().size());
        userRepository.deleteUser(uid3);
        Assertions.assertEquals(0, userRepository.getAllUsers().size());
        userRepository.deleteUser(12345L);
        Assertions.assertEquals(0, userRepository.getAllUsers().size());
    }
}