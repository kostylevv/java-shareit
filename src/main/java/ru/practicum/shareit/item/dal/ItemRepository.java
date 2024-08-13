package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item addItem(Item item);
    Item updateItem(Item item);
    Item getItem(long id);
    Collection<Item> getAllUserItems(long userId);
    Collection<Item> findAvailableItemsByNameOrDescription(String searchTerm);
    void deleteItem(long id);
}
