package ru.practicum.shareit.user.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
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
    public Optional<User> addUser(User user) {
        log.info("Add user {}", user);
        user.setId(getNextId());
        storage.put(user.getId(), user);
        log.info("User added: {}", user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (getUserById(user.getId()).isEmpty()) {
            log.error("User with id = {} doesn't exist, can't update", user.getId());
            return Optional.empty();
        }
        storage.put(user.getId(), user);
        return Optional.of(user);
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
