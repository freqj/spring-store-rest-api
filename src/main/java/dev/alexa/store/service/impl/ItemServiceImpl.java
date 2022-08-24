package dev.alexa.store.service.impl;

import dev.alexa.store.domain.Item;
import dev.alexa.store.domain.Message;
import dev.alexa.store.domain.User;
import dev.alexa.store.exception.ApiException;
import dev.alexa.store.exception.ResourceNotFoundException;
import dev.alexa.store.payload.CurrentUser;
import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.repository.ItemRepository;
import dev.alexa.store.repository.MessageRepository;
import dev.alexa.store.repository.UserRepository;
import dev.alexa.store.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public ItemDto createItem(ItemDto itemDto) {
        SecurityContext context = SecurityContextHolder.getContext();

        Item item = mapToItem(itemDto);
        String username = context.getAuthentication().getName();
        item.setOwner(userRepository.findByUsernameOrEmail(username, username).orElseThrow(()-> new ResourceNotFoundException("user", "username", username)));
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
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id.toString()));
        if(!user.getId().equals(item.getOwner().getId()) && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot change other users items");
        }
        item.setDescription(itemDto.getDescription());
        item.setPrice(itemDto.getPrice());
        item.setTitle(itemDto.getTitle());

        Item newItem = itemRepository.save(item);
        return mapToDto(newItem);
    }

    @Override
    public void deleteItemById(Long id) {
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id.toString()));
        if(!user.getId().equals(item.getOwner().getId()) && !user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot delete other users items");
        }
        itemRepository.delete(item);
    }

    @Override
    public void sendBuyRequestToOwner(Long id, String text) {
        SecurityContext context = SecurityContextHolder.getContext();
        User sender = userRepository.findByUsernameOrEmail(context.getAuthentication().getName(), context.getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with credentials:" + context.getAuthentication().getName()));
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("item", "id", id.toString()));
        if(sender.getId().equals(item.getOwner().getId()))
        {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot buy item from yourself");
        }
        User destination = item.getOwner();
        Message message = new Message();
        message.setMessage(text);
        message.setItem(item);
        message.setDestination(destination);
        message.setSender(sender);
        messageRepository.save(message);

    }

    @Override
    public ContentListResponse getAllItems(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

       Pageable pageable =  PageRequest.of(pageNumber,pageSize,sort);
        Page<Item> page = itemRepository.findAll(pageable);

        List<Item> listOfItems = page.getContent();
        List<ItemDto> content =    listOfItems.stream().map(item -> mapToDto(item)).collect(Collectors.toList());

        ContentListResponse<ItemDto> itemResponse = new ContentListResponse();
        itemResponse.setContent(content);
        itemResponse.setPageSize(page.getSize());
        itemResponse.setPageNumber(page.getNumber());
        itemResponse.setLast(page.isLast());
        itemResponse.setTotalElements(page.getTotalElements());
        itemResponse.setTotalPages(page.getTotalPages());

        return itemResponse;
    }

    @Override
    public ContentListResponse getAllUserItems(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable =  PageRequest.of(pageNumber,pageSize,sort);
        Page<Item> page = itemRepository.findAllByOwner_Id(pageable,user.getId());
        List<Item> listOfItems = page.getContent();
        List<ItemDto> content =    listOfItems.stream().map(item -> mapToDto(item)).collect(Collectors.toList());

        ContentListResponse<ItemDto> itemResponse = new ContentListResponse();
        itemResponse.setContent(content);
        itemResponse.setPageSize(page.getSize());
        itemResponse.setPageNumber(page.getNumber());
        itemResponse.setLast(page.isLast());
        itemResponse.setTotalElements(page.getTotalElements());
        itemResponse.setTotalPages(page.getTotalPages());

        return itemResponse;
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
