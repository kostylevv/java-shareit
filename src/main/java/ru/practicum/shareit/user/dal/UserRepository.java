package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    void deleteUser(long id);
}
