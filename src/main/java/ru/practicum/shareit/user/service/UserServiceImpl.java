package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(@Autowired UserRepository userRepository) {
        repository = userRepository;
    }

    @Override
    public UserDto getUser(long id) {
        Optional<User> user = repository.getUserById(id);
        if (user.isPresent()) {
            return UserMapper.mapToDto(user.get());
        } else {
            log.error("User with id = {} not found", id);
            throw new NotFoundException("User with id = " + id + "not found");
        }
    }

    @Override
    public UserDto getUser(String email) {
        Optional<User> user = repository.getUserByEmail(email);
        if (user.isPresent()) {
            return UserMapper.mapToDto(user.get());
        } else {
            log.error("User with email = {} not found", email);
            throw new NotFoundException("User with email = " + email + "not found");
        }
    }

    @Override
    public Collection<UserDto> getUsers() {
        return repository.getAllUsers().stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto createUser(NewUserDto dto) {
        log.info("Creating user wit dto {}", dto);
        if (dto == null || !isDtoValid(dto)) {
            throw new ValidationException("Validation failed for dto " + dto);
        }

        if (repository.getUserByEmail(dto.getEmail()).isPresent()) {
            log.error("Email {} already exists", dto.getEmail());
            throw new DuplicatedDataException("Email already exists");
        }

        Optional<User> user = repository.addUser(UserMapper.mapToUser(dto));
        if (user.isPresent()) {
            log.info("Created user {}", user.get());
            return UserMapper.mapToDto(user.get());
        } else {
            log.error("User is empty");
            throw new InternalServerException("Internal server exception");
        }
    }

    @Override
    public UserDto updateUser(UpdatedUserDto dto) {
        log.info("Updating user wit dto {}", dto);
        if (dto == null) {
            log.error("Validation failed for dto: {} ", dto);
            throw new ValidationException("Validation failed for dto " + dto);
        }
        UserDto existing = getUser(dto.getId());

        if (dto.getName() == null && dto.getEmail() == null) {
            log.error("Name and email are empty in updated user");
            throw new ValidationException("Name and email are empty in updated user");
        }

        if (dto.getEmail() == null) {
            dto.setEmail(existing.getEmail());
        } else if (!existing.getEmail().equals(dto.getEmail())
                && repository.getUserByEmail(dto.getEmail()).isPresent()) {
            log.error("Email {} already exists", dto.getEmail());
            throw new DuplicatedDataException("Email " + dto.getEmail() + "already exists");
        }

        if (dto.getName() == null) {
            dto.setName(existing.getName());
        }

        Optional<User> user = repository.updateUser(UserMapper.mapToUser(dto));
        if (user.isPresent()) {
            log.info("User updated: {}", user.get());
            return UserMapper.mapToDto(user.get());
        } else {
            throw new InternalServerException("Internal server exception");
        }
    }

    @Override
    public void deleteUser(long id) {
        if (repository.getUserById(id).isEmpty()) {
            log.error("User with id {} doesn't exist, can't delete", id);
        } else {
            repository.deleteUser(id);
            log.info("Deleted user with id {}", id);
        }
    }

    private boolean isDtoValid(NewUserDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            log.error("DTO's name is null or blank");
            return false;
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            log.error("DTO's email is null or blank");
            return false;
        }
        return true;
    }
}
