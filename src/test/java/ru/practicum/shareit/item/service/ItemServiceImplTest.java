package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dal.InMemoryItemRepo;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.user.dal.InMemoryUserRepo;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

class ItemServiceImplTest {
    UserService userService;
    ItemService itemService;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(new InMemoryUserRepo());
        itemService = new ItemServiceImpl(userService, new InMemoryItemRepo());
    }

    @Test
    void createItem() {
        UserDto user = userService.createUser(NewUserDto.builder()
                .email("test@test.com")
                .name("Test")
                .build());
        ItemDto item = itemService.createItem(NewItemDto.builder()
                .name("test item")
                .description("desc")
                .available(true)
                .ownerId(user.getId()).build());
        Assertions.assertEquals(user.getId(), item.getOwnerId());
        Assertions.assertEquals("test item", item.getName());
        Assertions.assertEquals("desc", item.getDescription());
        Assertions.assertTrue(item.isAvailable());
    }

    @Test
    void createItemWithoutOwnerFail() {
        Assertions.assertThrows(BadRequestException.class, () -> itemService.createItem(NewItemDto.builder()
                .name("test item")
                .description("desc")
                .available(true)
                .build()));
    }


    @Test
    void updateItem() {
        UserDto user = userService.createUser(NewUserDto.builder()
                .email("test@test.com")
                .name("Test")
                .build());
        ItemDto item = itemService.createItem(NewItemDto.builder()
                .name("test item")
                .description("desc")
                .available(true)
                .ownerId(user.getId()).build());
        UpdatedItemDto updatedItemDto = UpdatedItemDto.builder()
                .id(item.getId())
                .name("updated name")
                .description("updated desc")
                .available(false).build();

        ItemDto updated = itemService.updateItem(updatedItemDto);
        Assertions.assertEquals(item.getId(), updated.getId());
        Assertions.assertEquals("updated name", updated.getName());
        Assertions.assertEquals("updated desc", updated.getDescription());
        Assertions.assertFalse(updated.isAvailable());
        Assertions.assertEquals(user.getId(), updated.getOwnerId());
    }

    @Test
    void updateOnlyName() {
        UserDto user = userService.createUser(NewUserDto.builder()
                .email("test@test.com")
                .name("Test")
                .build());
        ItemDto item = itemService.createItem(NewItemDto.builder()
                .name("test item")
                .description("desc")
                .available(true)
                .ownerId(user.getId()).build());
        UpdatedItemDto updatedItemDto = UpdatedItemDto.builder()
                .id(item.getId())
                .name("updated name").build();

        ItemDto updated = itemService.updateItem(updatedItemDto);
        Assertions.assertEquals(item.getId(), updated.getId());
        Assertions.assertEquals("updated name", updated.getName());
        Assertions.assertEquals(item.getDescription(), updated.getDescription());
        Assertions.assertTrue(updated.isAvailable());
        Assertions.assertEquals(user.getId(), updated.getOwnerId());
    }

    @Test
    void updateItemThatDoesNotExistFail() {
        UpdatedItemDto updatedItemDto = UpdatedItemDto.builder()
                .name("updated name")
                .description("updated desc")
                .available(false).build();
        Assertions.assertThrows(BadRequestException.class, () -> itemService.updateItem(updatedItemDto));
    }

}