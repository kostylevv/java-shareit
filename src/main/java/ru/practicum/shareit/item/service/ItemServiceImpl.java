package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.util.List;
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
    public ItemDto updateItem(UpdatedItemDto itemDto) {
        ItemDto existing = getItem(itemDto.getId());
        //return new ItemDto()''
    }

    @Override
    public ItemDto getItem(long id) {
        log.info("Getting item with id = {}", id);
        return ItemMapper.mapToDto(itemRepository.getItem(id));
    }

    @Override
    public Collection<ItemDto> getAllUserItems(long userId) {
        return List.of();
    }

    @Override
    public Collection<ItemDto> findAllAvailableItemsByNameOrDescription(String searchTerm) {
        return List.of();
    }

    @Override
    public void deleteItem(long id) {

    }
}
