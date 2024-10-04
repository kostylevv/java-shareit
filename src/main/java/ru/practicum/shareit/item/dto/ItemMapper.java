package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    private ItemMapper() {
    }

    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .userId(item.getOwner().getId())
                .build();
    }

    public static Item mapToItem(NewItemDto newItemDto, User user) {
        Item item = new Item();
        item.setName(newItemDto.getName());
        item.setDescription(newItemDto.getDescription());
        item.setOwner(user);
        item.setAvailable(newItemDto.getAvailable());
        return item;
    }

    public static Item mapToItem(Item existing, UpdatedItemDto updatedItemDto) {
        String name, description;
        boolean available;

        if (updatedItemDto.getName() == null || updatedItemDto.getName().isEmpty()) {
            name = existing.getName();
        } else {
            name = updatedItemDto.getName();
        }

        if (updatedItemDto.getDescription() == null || updatedItemDto.getDescription().isEmpty()) {
            description = existing.getDescription();
        } else {
            description = updatedItemDto.getDescription();
        }

        if (updatedItemDto.getAvailable() == null) {
            available = existing.isAvailable();
        } else {
            available = updatedItemDto.getAvailable();
        }

        Item item = new Item();
        item.setId(existing.getId());
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setItemRequest(existing.getItemRequest());
        item.setOwner(existing.getOwner());
        return item;
    }

}
