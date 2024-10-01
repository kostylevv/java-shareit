package ru.practicum.shareit.item.dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryItemRepoTest {
    ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        itemRepository = new InMemoryItemRepo();
    }

    @Test
    void addItem() {
        Item item = Item.builder().build();
        Item added = itemRepository.addItem(item);
        assertNotNull(added);
    }

    @Test
    void updateItem() {
        Item item = Item.builder().name("item 1").description("item 1 desc").build();
        Item added = itemRepository.addItem(item);
        assertEquals(item.getName(), added.getName());
        assertEquals(item.getDescription(), added.getDescription());
        Item updatedItem = Item.builder().name("item 1 updated").description("item 1 desc updated").id(added.getId()).build();
        Item updated = itemRepository.updateItem(updatedItem);
        assertEquals(updatedItem.getName(), updated.getName());
        assertEquals(updatedItem.getDescription(), updated.getDescription());
        assertEquals(added.getId(), updated.getId());
    }

    @Test
    void updateItemNullIdFails() {
        Item item = Item.builder().name("item 1").description("item 1 desc").build();
        assertThrows(NotFoundException.class, () -> itemRepository.updateItem(item));
    }

    @Test
    void updateItemUnexsitingIdFails() {
        Item item = Item.builder().id(1234L).name("item 1").description("item 1 desc").build();
        assertThrows(NotFoundException.class, () -> itemRepository.updateItem(item));
    }

    @Test
    void getItem() {
        Item item1 = Item.builder().name("item 1").description("item 1 desc").build();
        Item item2 = Item.builder().name("item 2").description("item 2 desc").build();
        Item item3 = Item.builder().name("item 3").description("item 3 desc").build();

        Item added1 = itemRepository.addItem(item1);
        Item added2 = itemRepository.addItem(item2);
        Item added3 = itemRepository.addItem(item3);

        Item requested1 = itemRepository.getItem(added1.getId());
        assertEquals(added1, requested1);
        Item requested2 = itemRepository.getItem(added2.getId());
        assertEquals(added2, requested2);
        Item requested3 = itemRepository.getItem(added3.getId());
        assertEquals(added3, requested3);

        assertThrows(NotFoundException.class, () -> itemRepository.getItem(10L));
    }

    @Test
    void getAllUserItems() {
        int evenCount = 0;
        int oddCount = 0;
        for (int i = 1; i < 1000; i++) {
            if (i % 2 == 0) {
                Item item1 = Item.builder().ownerId(2).name("item " + i).description("item " + i + " desc").build();
                evenCount++;
                itemRepository.addItem(item1);
            } else {
                Item item1 = Item.builder().ownerId(1).name("item " + i).description("item " + i + " desc").build();
                oddCount++;
                itemRepository.addItem(item1);
            }
        }

        assertEquals(evenCount, itemRepository.getAllUserItems(2).size());
        assertEquals(oddCount, itemRepository.getAllUserItems(1).size());
        assertEquals(0, itemRepository.getAllUserItems(3).size());

    }

    @Test
    void findAvailableItemsByNameOrDescription() {
        Item item1 = Item.builder().name("philips screwdriver").available(true).description("almost new").build();
        Item item2 = Item.builder().name("hammer").available(true).description("heavy wood hammer").build();
        Item item3 = Item.builder().name("screwdriver").available(true).description("made by hazet").build();
        Item item4 = Item.builder().name("toaster").available(true).description("philips toaster").build();
        Item item5 = Item.builder().name("hand tool set").description("a lot of useful tools including screwdriver").build();
        Item item6 = Item.builder().name("knife").available(true).description("18 cm, new").build();
        itemRepository.addItem(item1);
        itemRepository.addItem(item2);
        itemRepository.addItem(item3);
        itemRepository.addItem(item4);
        itemRepository.addItem(item5);
        itemRepository.addItem(item6);

        assertEquals(2, itemRepository.findAvailableItemsByNameOrDescription("sCrewDRIver").size());
        assertEquals(2, itemRepository.findAvailableItemsByNameOrDescription("philips").size());
        assertEquals(2, itemRepository.findAvailableItemsByNameOrDescription("new").size());
        assertEquals(1, itemRepository.findAvailableItemsByNameOrDescription("hammer").size());
        assertEquals(1, itemRepository.findAvailableItemsByNameOrDescription("knife").size());

    }
}