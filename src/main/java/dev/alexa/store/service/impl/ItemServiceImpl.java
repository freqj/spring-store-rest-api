package dev.alexa.store.service.impl;

import dev.alexa.store.domain.Item;
import dev.alexa.store.exception.ResourceNotFoundException;
import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.payload.ItemListResponse;
import dev.alexa.store.repository.ItemRepository;
import dev.alexa.store.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ItemListResponse getAllItems(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

       Pageable pageable =  PageRequest.of(pageNumber,pageSize,sort);
        Page<Item> page = itemRepository.findAll(pageable);

        List<Item> listOfItems = page.getContent();
        List<ItemDto> content =    listOfItems.stream().map(item -> mapToDto(item)).collect(Collectors.toList());

        ItemListResponse itemResponse = new ItemListResponse();
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
