package dev.alexa.store.service;


import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.payload.ContentListResponse;


public interface ItemService {

    ItemDto createItem(ItemDto itemDto);
    ItemDto getItemById(Long id);
    ItemDto updateItem(ItemDto itemDto, Long id);
    void deleteItemById(Long id);

    ContentListResponse getAllItems(int pageNumber, int pageSize, String sortBy, String sortDir);
}
