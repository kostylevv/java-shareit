package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dal.InMemoryItemRepo;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.InMemoryUserRepo;
import ru.practicum.shareit.user.dal.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceImplTest {
    UserRepository userRepo;
    ItemRepository itemRepository;

    UserService userService;
    ItemService itemService;
    User user1, user2;

    @BeforeEach
    void beforeEach() {
        itemRepository = new InMemoryItemRepo();
        userRepo = new InMemoryUserRepo();
        itemService = new ItemServiceImpl(userRepo, itemRepository);
        userService = new UserServiceImpl(userRepo);

        user1 = User.builder().name("Name 1").email("n1@e.com").build();
        user2 = User.builder().name("Name 2").email("n2@e.com").build();

    }

    @Test
    void createItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void getAllUserItems() {
    }

    @Test
    void findAllAvailableItemsByNameOrDescription() {
    }
}