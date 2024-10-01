package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable("userId") @Positive long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserDto userRequest) {
        return userService.createUser(userRequest);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable("userId") @Positive long userId, @Valid @RequestBody UpdatedUserDto updatedUser) {
        updatedUser.setId(userId);
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") @Positive long userId) {
        userService.deleteUser(userId);
    }
}
