package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(NewItemDto itemDto);

    ItemDto updateItem(UpdatedItemDto itemDto);

    ItemDto getItem(long id);

    Collection<ItemDto> getAllUserItems(long userId);

    Collection<ItemDto> findAllAvailableItemsByNameOrDescription(String searchTerm);

    void deleteItem(long id);
}
