package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto getUser(long id);

    UserDto getUser(String email);

    Collection<UserDto> getUsers();

    UserDto createUser(NewUserDto dto);

    UserDto updateUser(UpdatedUserDto dto);

    void deleteUser(long id);
}
