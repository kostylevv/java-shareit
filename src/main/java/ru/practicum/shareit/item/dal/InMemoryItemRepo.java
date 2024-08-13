package ru.practicum.shareit.item.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class InMemoryItemRepo implements ItemRepository {
    private final Map<Long, Item> storage = new HashMap<>();
    private long id = 0L;

    @Override
    public Item addItem(Item item) {
        log.info("Add item {}", item);
        item.setId(getNextId());
        storage.put(item.getId(), item);
        log.info("Item added: {}", item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (item.getId() != null && storage.containsKey(item.getId())) {
            storage.put(item.getId(), item);
            return item;
        } else {
            log.error("Item with id = {} not found, can't update", item.getId());
            throw new NotFoundException("Item with id " + item.getId() + " not found");
        }
    }

    @Override
    public Item getItem(long id) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        } else {
            log.error("Item with id = {} not found, can't get", id);
            throw new NotFoundException("Item with id " + id + " not found");
        }
    }

    @Override
    public Collection<Item> getAllUserItems(long userId) {
        log.info("Get all items from user {}", userId);
        return storage.values().stream().filter(item -> item.getOwnerId() == userId).toList();
    }

    @Override
    public Collection<Item> findAvailableItemsByNameOrDescription(String searchTerm) {
        log.info("Find available items by name or description {}", searchTerm);
        return storage.values()
                .stream()
                .filter(item -> item.isAvailable() && (item.getName().toUpperCase().contains(searchTerm.toUpperCase()) ||
                        item.getDescription().toUpperCase().contains(searchTerm.toUpperCase())))
                .toList();
    }

    @Override
    public void deleteItem(long id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            log.info("Deleted item {}", id);
        } else {
            log.warn("Item with id {} not found, can't delete", id);
        }
    }

    private long getNextId() {
        return id++;
    }
}
