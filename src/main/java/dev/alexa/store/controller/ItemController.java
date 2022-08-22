package dev.alexa.store.controller;

import dev.alexa.store.domain.Item;
import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.service.ItemService;
import dev.alexa.store.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable(name = "id") Long id)
    {
        ItemDto itemDto = itemService.getItemById(id);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public ContentListResponse<ItemDto> getAllItems(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value = "by", defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sort", defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false)String sortDir
    ){
       return itemService.getAllItems(pageNumber, pageSize, sortBy, sortDir);
    }
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto)
    {
        ItemDto item = itemService.createItem(itemDto);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteItem(@PathVariable(name = "id") Long id)
    {
        itemService.deleteItemById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ItemDto> updateItem(@Valid @RequestBody ItemDto itemDto, @PathVariable(name = "id") Long id)
    {
        ItemDto updatedItem = itemService.updateItem(itemDto, id);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }







}
