package dev.alexa.store.service.impl;

import dev.alexa.store.domain.Item;
import dev.alexa.store.exception.ResourceNotFoundException;
import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.repository.ItemRepository;
import dev.alexa.store.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Item item = mapToItem(itemDto);
        Item repoItem = itemRepository.save(item);
        ItemDto savedItem = mapToDto(repoItem);
        return savedItem;
    }



    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Item", "id", id.toString()));
        return mapToDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id.toString()));

        item.setDescription(itemDto.getDescription());
        item.setPrice(itemDto.getPrice());
        item.setTitle(itemDto.getTitle());

        Item newItem = itemRepository.save(item);
        return mapToDto(newItem);
    }

    @Override
    public void deleteItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id.toString()));
        itemRepository.delete(item);
    }

    private ItemDto mapToDto(Item item)
    {
        ItemDto itemDto = mapper.map(item, ItemDto.class);
        return itemDto;
    }

    private Item mapToItem(ItemDto itemDto)
    {
        Item item = mapper.map(itemDto, Item.class);
        return item;
    }
}
