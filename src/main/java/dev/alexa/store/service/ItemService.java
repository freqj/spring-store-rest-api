package dev.alexa.store.service;


import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.payload.ContentListResponse;
import org.apache.coyote.ContinueResponseTiming;


public interface ItemService {

    ItemDto createItem(ItemDto itemDto);
    ItemDto getItemById(Long id);
    ItemDto updateItem(ItemDto itemDto, Long id);
    void deleteItemById(Long id);
    void sendBuyRequestToOwner(Long id, String text);
    ContentListResponse getAllItems(int pageNumber, int pageSize, String sortBy, String sortDir);

    ContentListResponse getAllUserItems(int pageNumber, int pageSize, String sortBy, String sortDir);
}
