package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.InMemoryUserRepo;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    UserRepository repository;
    UserService service;
    User user1, user2, user3;

    @BeforeEach
    void beforeEach() {
        repository = new InMemoryUserRepo();
        service = new UserServiceImpl(repository);
        user1 = User.builder().name("Name 1").email("n1@e.com").build();
        user2 = User.builder().name("Name 2").email("n2@e.com").build();
        user3 = User.builder().name("Name 3").email("n3@e.com").build();
    }

    @Test
    void getUserId() {
        long uid1 = repository.addUser(user1).get().getId();
        long uid2 = repository.addUser(user2).get().getId();
        assertEquals(uid1, service.getUser(uid1).getId());
        assertEquals(uid2, service.getUser(uid2).getId());
        assertThrows(NotFoundException.class, () -> service.getUser(1235));
    }

    @Test
    void getUserEmail() {
        long uid1 = repository.addUser(user1).get().getId();
        long uid2 = repository.addUser(user2).get().getId();
        assertEquals(uid1, service.getUser(user1.getEmail()).getId());
        assertEquals(uid2, service.getUser(user2.getEmail()).getId());
        assertThrows(NotFoundException.class, () -> service.getUser(user3.getEmail()));
    }

    @Test
    void getUsers() {
        assertTrue(service.getUsers().isEmpty());
        repository.addUser(user1);
        repository.addUser(user2);
        repository.addUser(user3);
        assertEquals(3, service.getUsers().size());
    }

    @Test
    void createUserOk() {
        NewUserDto newUserDto = NewUserDto.builder()
                .name("new name")
                .email("newuser@email.com").build();
        UserDto added = service.createUser(newUserDto);
        assertNotNull(added);
        assertEquals(newUserDto.getName(), added.getName());
        assertEquals(newUserDto.getEmail(), added.getEmail());
        assertTrue(added.getId() >= 0);
    }

    @Test
    void createUserDuplicateEmailNotOk() {
        repository.addUser(user1);
        NewUserDto newUserDto = NewUserDto.builder()
                .name("new name")
                .email(user1.getEmail()).build();
        assertThrows(DuplicatedDataException.class, () -> service.createUser(newUserDto));
    }

    @Test
    void updateUser() {
        long uid1 = repository.addUser(user1).get().getId();
        UpdatedUserDto updateRequest = UpdatedUserDto.builder()
                .id(uid1)
                .name("updated")
                .email("updated@email.com")
                .build();
        UserDto updated = service.updateUser(updateRequest);
        assertEquals(updateRequest.getEmail(), updated.getEmail());
        assertEquals(updateRequest.getName(), updated.getName());
        assertEquals(uid1, updated.getId());
    }

    @Test
    void updateNameOnly() {
        User added = repository.addUser(user1).get();
        UpdatedUserDto updateRequest = UpdatedUserDto.builder()
                .id(added.getId())
                .name("updated only name")
                .build();
        UserDto updated = service.updateUser(updateRequest);
        assertEquals(added.getEmail(), updated.getEmail());
        assertEquals(updateRequest.getName(), updated.getName());
        assertEquals(added.getId(), updated.getId());
    }

    @Test
    void updateEmailOnly() {
        User added = repository.addUser(user1).get();
        UpdatedUserDto updateRequest = UpdatedUserDto.builder()
                .id(added.getId())
                .email("updated@email.only.com")
                .build();
        UserDto updated = service.updateUser(updateRequest);
        assertEquals(updateRequest.getEmail(), updated.getEmail());
        assertEquals(added.getName(), updated.getName());
        assertEquals(added.getId(), updated.getId());
    }

    @Test
    void updateNothingFails() {
        User added = repository.addUser(user1).get();
        UpdatedUserDto updateRequest = UpdatedUserDto.builder()
                .id(added.getId())
                .build();
        assertThrows(ValidationException.class, () -> service.updateUser(updateRequest));
    }

    @Test
    void updateWrongIdFails() {
        User added = repository.addUser(user1).get();
        UpdatedUserDto updateRequest = UpdatedUserDto.builder()
                .id(12123L)
                .name("updated wrong id")
                .email("updated wrong email")
                .build();
        assertThrows(NotFoundException.class, () -> service.updateUser(updateRequest));
    }

    @Test
    void deleteUser() {
    }
}