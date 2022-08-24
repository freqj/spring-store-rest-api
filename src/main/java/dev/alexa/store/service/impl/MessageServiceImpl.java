package dev.alexa.store.service.impl;

import dev.alexa.store.domain.Item;
import dev.alexa.store.domain.Message;
import dev.alexa.store.domain.User;
import dev.alexa.store.exception.ResourceNotFoundException;
import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.payload.CurrentUser;
import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.payload.MessageDto;
import dev.alexa.store.repository.MessageRepository;
import dev.alexa.store.service.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MessageRepository messageRepository;
    @Override
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public MessageDto getMessageById(Long id) {


        return mapToDto(messageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("message", "id", id.toString())));
    }

    @Override
    public ContentListResponse<MessageDto> getAllUserMessages(int pageNumber, int pageSize, String sortBy, String sortDir) {
        SecurityContext context = SecurityContextHolder.getContext();
        CurrentUser user  = (CurrentUser) context.getAuthentication().getPrincipal();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Message> page = messageRepository.findAllByDestination_Id(user.getId(), pageable);
        List<Message> listOfMessages = page.getContent();
        List<MessageDto> content = listOfMessages.stream().map(message -> mapToDto(message)).collect(Collectors.toList());

        ContentListResponse<MessageDto> response = new ContentListResponse<>();
        response.setContent(content);
        response.setPageSize(page.getSize());
        response.setPageNumber(page.getNumber());
        response.setLast(page.isLast());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }

    private MessageDto mapToDto(Message message)
    {
        MessageDto messageDto = mapper.map(message, MessageDto.class);
        return messageDto;
    }

    private Message mapToItem(MessageDto messageDto)
    {
        Message message = mapper.map(messageDto, Message.class);
        return message;
    }
}
