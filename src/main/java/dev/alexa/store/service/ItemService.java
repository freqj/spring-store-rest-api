package dev.alexa.store.service;


import dev.alexa.store.payload.ItemDto;
import org.springframework.stereotype.Service;


public interface ItemService {

    ItemDto createItem(ItemDto itemDto);
    ItemDto getItemById(Long id);
    ItemDto updateItem(ItemDto itemDto, Long id);
    void deleteItemById(Long id);

}
