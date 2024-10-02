package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
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
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return UserMapper.mapToDto(user.get());
        } else {
            log.error("User with id = {} not found", id);
            throw new NotFoundException("User with id = " + id + "not found");
        }
    }

    @Override
    public UserDto getUser(String email) {
        Optional<User> user = repository.findByEmailContainingIgnoreCase(email);
        if (user.isPresent()) {
            return UserMapper.mapToDto(user.get());
        } else {
            log.error("User with email = {} not found", email);
            throw new NotFoundException("User with email = " + email + "not found");
        }
    }

    @Override
    public Collection<UserDto> getUsers() {
        return repository.findAll().stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto createUser(NewUserDto dto) {
        log.info("Creating user wit dto {}", dto);

        if (repository.findByEmailContainingIgnoreCase(dto.getEmail()).isPresent()) {
            log.error("Email {} already exists", dto.getEmail());
            throw new DuplicatedDataException("Email already exists");
        }

        User user = repository.save(UserMapper.mapToUser(dto));

        log.info("Created user {}", user);
        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UpdatedUserDto dto) {
        log.info("Updating user wit dto {}", dto);
        if (dto == null) {
            log.error("Validation failed for dto: {} ", dto);
            throw new ValidationException("Validation failed for dto " + dto);
        }
        User existing = repository.findById(dto.getId()).orElseThrow();

        if (dto.getName() == null && dto.getEmail() == null) {
            log.error("Name and email are empty in updated user");
            throw new ValidationException("Name and email are empty in updated user");
        }

        if (dto.getEmail() != null) {
            if (!existing.getEmail().equals(dto.getEmail())
                    && repository.findByEmailContainingIgnoreCase(dto.getEmail()).isPresent()) {
                log.error("Email {} already exists", dto.getEmail());
                throw new DuplicatedDataException("Email " + dto.getEmail() + "already exists");
            } else {
                existing.setEmail(dto.getEmail());
            }
        }

        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }

        User user = repository.save(existing);

        log.info("User updated: {}", user);
        return UserMapper.mapToDto(user);

    }

    @Override
    public void deleteUser(long id) {
        if (repository.findById(id).isEmpty()) {
            log.error("User with id {} doesn't exist, can't delete", id);
        } else {
            repository.deleteById(id);
            log.info("Deleted user with id {}", id);
        }
    }

}
