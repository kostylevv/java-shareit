package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    private ItemMapper() {}

    public static ItemDto mapToDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .ownerId(item.getOwnerId())
                .build();
        if (item.getItemRequestId() != null) {
            itemDto.setItemRequestId(item.getItemRequestId());
        }
        return itemDto;
    }

    public static Item mapToItem(NewItemDto newItemDto) {
        Item item = Item.builder()
                .name(newItemDto.getName())
                .description(newItemDto.getDescription())
                .available(newItemDto.getAvailable())
                .ownerId(newItemDto.getOwnerId())
                .build();
        if (newItemDto.getItemRequestId() > 0L) {
            item.setItemRequestId(newItemDto.getItemRequestId());
        }
        return item;
    }

}
