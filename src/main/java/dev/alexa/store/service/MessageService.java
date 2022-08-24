package dev.alexa.store.service;


import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.payload.MessageDto;

public interface MessageService {

    void deleteMessageById(Long id);
    MessageDto getMessageById(Long id);
    ContentListResponse<MessageDto> getAllUserMessages(int pageNumber, int pageSize, String sortBy, String sortDir);
}
