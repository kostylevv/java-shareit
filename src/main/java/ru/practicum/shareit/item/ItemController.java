package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
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
    public List<ItemDto> getAllUserItems(@RequestHeader(HEADER_PARAM_UID)  @PositiveOrZero long userId) {
        return service.getAllUserItems(userId).stream().toList();
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable("itemId") @PositiveOrZero long itemId) {
        return service.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_PARAM_UID) @PositiveOrZero long userId,
                          @PathVariable("itemId") @PositiveOrZero long itemId,
                          @RequestBody UpdatedItemDto item) {
        item.setId(itemId);
        item.setOwnerId(userId);
        return service.updateItem(item);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(HEADER_PARAM_UID) @PositiveOrZero long userId, @Valid @RequestBody NewItemDto item) {
        item.setOwnerId(userId);
        return service.createItem(item);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam @NotNull String text) {
        return service.findAllAvailableItemsByNameOrDescription(text);
    }

}
