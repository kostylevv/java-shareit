package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_PARAM_UID = "X-Sharer-User-Id";

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader(HEADER_PARAM_UID) long userId) {
        return service.getAllUserItems(userId).stream().toList();
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable("itemId") @Positive long itemId) {
        return service.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_PARAM_UID) long userId,
                          @PathVariable("itemId") @Positive long itemId,
                          @Valid @RequestBody UpdatedItemDto item) {
        item.setId(itemId);
        item.setOwnerId(userId);
        return service.updateItem(item);
    }

    @PatchMapping
    public ItemDto create(@RequestHeader(HEADER_PARAM_UID) long userId, @Valid @RequestBody NewItemDto item) {
        item.setOwnerId(userId);
        return service.createItem(item);
    }


}
