package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(long id);
    Optional<User> getUserByEmail(String email);
    Collection<User> getAllUsers();
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(long id);
}
