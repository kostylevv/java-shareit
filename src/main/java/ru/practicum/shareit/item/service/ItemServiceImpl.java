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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemServiceImpl(@Autowired UserService userService, @Autowired ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
    }


    @Override
    public ItemDto createItem(NewItemDto itemDto) {
        log.info("Adding item from itemDto {}", itemDto);
        UserDto userDto = userService.getUser(itemDto.getOwnerId());
        Item item = itemRepository.addItem(ItemMapper.mapToItem(itemDto));
        log.info("Added item {}", item);
        return ItemMapper.mapToDto(item);
    }

    @Override
    public ItemDto updateItem(UpdatedItemDto updatedItemDto) {
        log.info("Updating item {}", updatedItemDto);
        ItemDto existing = getItem(updatedItemDto.getId());
        if (updatedItemDto.getOwnerId() != existing.getOwnerId()) {
            log.error("Item with id {} has owner with id {}, " +
                            "got update from owner with id {}. Can't update", existing.getId(),
                    existing.getOwnerId(), updatedItemDto.getOwnerId());
            throw new NotFoundException("Owner ID's does not match, can't update item");
        }
        log.info("Existing item {}", existing);
        Item item = ItemMapper.mapToItem(existing, updatedItemDto);
        log.info("Updated item {}", item);
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
        try {
            UserDto userDto = userService.getUser(userId);
        } catch (NotFoundException e) {
            log.error("No user found with userId = {}", userId);
            throw new BadRequestException(e.getMessage());
        }
        return itemRepository.getAllUserItems(userId).stream().map(ItemMapper::mapToDto).toList();

    }

    @Override
    public Collection<ItemDto> findAllAvailableItemsByNameOrDescription(String searchTerm) {
        log.info("Finding available items by name or description containing search term {}:", searchTerm);
        if (searchTerm == null) {
            log.error("Search term is null");
            throw new BadRequestException("Search term is null");
        }

        if (searchTerm.isEmpty() || searchTerm.isBlank()) {
            log.warn("Search term is empty");
            return List.of();
        }

        return itemRepository.findAvailableItemsByNameOrDescription(searchTerm)
                .stream()
                .map(ItemMapper::mapToDto).toList();
    }

    @Override
    public void deleteItem(long id) {
        log.info("Deleting item with id = {}", id);
        itemRepository.deleteItem(id);
    }
}
