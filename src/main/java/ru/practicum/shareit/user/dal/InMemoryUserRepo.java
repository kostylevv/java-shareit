package ru.practicum.shareit.user.dal;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Slf4j
public class InMemoryUserRepo implements UserRepository {
    private final Map<Long, User> storage = new HashMap<>();
    private long id = 0L;

    @Override
    public Optional<User> getUserById(long id) {
        if (storage.containsKey(id)) {
            log.info("Returned user by id = {} : {}", id, Optional.of(storage.get(id)));
            return Optional.of(storage.get(id));
        } else {
            log.warn("User with id = {} not found ", id);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> result = storage.values().stream()
                .filter(user -> user.getEmail().trim().equalsIgnoreCase(email.trim()))
                .findFirst();
        if (result.isPresent()) {
            log.info("Returned user by email = {} : {}", email, result.get());
        } else {
            log.warn("User with email = {} not found ", email);
            return Optional.empty();
        }
        return result;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Returned users : {}", storage.values());
        return storage.values();
    }

    @Override
    public User addUser(User user) {
        log.info("Add user {}", user);
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Email is blank for user {}, can't add", user);
            throw new ValidationException("Email is blank for user " + user + ", can't add");
        }
        if (getUserByEmail(user.getEmail()).isPresent()) {
            log.error("Duplicate email for user {}, can't add", user);
            throw new DuplicatedDataException("Duplicate email for user " + user + ", can't add");
        }
        user.setId(getNextId());
        storage.put(user.getId(), user);
        log.info("User added: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserById(user.getId()).isEmpty()) {
            log.error("User with id = {} doesn't exist, can't update", user.getId());
            throw new NotFoundException("User with id =  " + user.getId() + " doesn't exist, can't update");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Email is blank for user {}, can't update", user);
            throw new ValidationException("Email is blank for user " + user + ", can't update");
        }

        Optional<User> userWithNewEmail = storage.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(user.getEmail()) && u.getId() != user.getId()).findFirst();

        if (userWithNewEmail.isPresent()) {
            log.error("Duplicate email: {}. This email is already in use by user:  {}", user.getEmail(), userWithNewEmail);
            throw new DuplicatedDataException("Duplicate email for user " + user + ", can't update");
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        if (storage.containsKey(id)) {
            log.info("Deleted user with id = {}", id);
            storage.remove(id);
        } else {
            log.warn("User with id = {} doesn't exist, can't delete", id);
        }
    }

    private long getNextId() {
        return id++;
    }
}
