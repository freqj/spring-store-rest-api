package dev.alexa.store.controller;

import dev.alexa.store.payload.ItemDto;
import dev.alexa.store.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto)
    {
        ItemDto item = itemService.createItem(itemDto);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem(@PathVariable(name = "id") Long id)
    {
        itemService.deleteItemById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@Valid @RequestBody ItemDto itemDto, @PathVariable(name = "id") Long id)
    {
        ItemDto updatedItem = itemService.updateItem(itemDto, id);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }







}
