package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(@Autowired UserRepository userRepository, @Autowired ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public ItemDto createItem(NewItemDto itemDto) {
        Optional<User> user = userRepository.getUserById(itemDto.getOwnerId());
        if (user.isPresent()) {
            log.info("Adding item from itemDto {}", itemDto);
            Item item = itemRepository.addItem(ItemMapper.mapToItem(itemDto));
            log.info("Added item {}", item);
            return ItemMapper.mapToDto(item);
        } else {
            log.error("User with id = {} not found", itemDto.getOwnerId());
            throw new NotFoundException("User with id " + itemDto.getOwnerId() + " not found");
        }
    }

    @Override
    public ItemDto updateItem(UpdatedItemDto updatedItemDto) {
        log.info("Updating item {}", updatedItemDto);
        ItemDto existing = getItem(updatedItemDto.getId());
        log.info("Existing item {}", existing);
        Item item = ItemMapper.mapToItem(existing, updatedItemDto);
        return ItemMapper.mapToDto(item);
    }

    @Override
    public ItemDto getItem(long id) {
        log.info("Getting item with id = {}", id);
        return ItemMapper.mapToDto(itemRepository.getItem(id));
    }

    @Override
    public Collection<ItemDto> getAllUserItems(long userId) {
        log.info("Getting user items with userId = {}", userId);
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isPresent()) {
            return itemRepository.getAllUserItems(userId).stream().map(ItemMapper::mapToDto).toList();
        } else {
            log.error("User with id = {} not found", userId);
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    @Override
    public Collection<ItemDto> findAllAvailableItemsByNameOrDescription(String searchTerm) {
        log.info("Finding available items by name or description containing search term {}:", searchTerm);
        if (searchTerm != null && !searchTerm.isBlank()) {
            return itemRepository.findAvailableItemsByNameOrDescription(searchTerm)
                    .stream()
                    .map(ItemMapper::mapToDto).toList();
        } else {
            log.error("Search term is null or blank");
            throw new BadRequestException("Search term is null or blank");
        }

    }

    @Override
    public void deleteItem(long id) {
        log.info("Deleting item with id = {}", id);
        itemRepository.deleteItem(id);
    }
}
