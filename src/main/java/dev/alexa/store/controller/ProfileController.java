package dev.alexa.store.controller;

import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.payload.*;
import dev.alexa.store.security.View;
import dev.alexa.store.service.ItemService;
import dev.alexa.store.service.MessageService;
import dev.alexa.store.service.UserService;
import dev.alexa.store.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/profile")
@Api(value = "CRUD REST API for authenticated user")
public class ProfileController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;


    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Get information of authenticated user")
    public ResponseEntity<UserDto> getUserProfile(@AuthenticationPrincipal CurrentUser currentUser)
    {
        UserDto userProfile = userService.getUserById(currentUser.getId());
        return new ResponseEntity(userProfile, HttpStatus.OK);
    }

    @GetMapping("/message")
    @PreAuthorize("hasRole('USER')")
    @JsonView({View.ResponseView.MinimalView.class})
    @ApiOperation(value = "Get income buy requests of authenticated user")
    public ContentListResponse<MessageDto> getUserMessages(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
            @RequestParam(value = "by", defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sort", defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false)String sortDir
    )
    {
        return messageService.getAllUserMessages(pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("message/{id}")
    @PreAuthorize("hasRole('USER')")
    @JsonView({View.MessageView.FullInfo.class})
    @ApiOperation(value = "Get full view of buy request")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable(name = "id") Long id)
    {
        return new ResponseEntity(messageService.getMessageById(id), HttpStatus.OK);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Update authenticated user information")
    public ResponseEntity<UserDto> changeUser(@RequestBody UserDto userDto, @AuthenticationPrincipal CurrentUser user)
    {
        userDto.setId(user.getId());
        UserDto updatedUser = userService.updateUser(userDto,user.getId());

        return new ResponseEntity(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/items")
    @PreAuthorize("hasRole('USER')")
    @JsonView(View.ResponseView.MinimalView.class)
    @ApiOperation(value = "Get all your items")
    ContentListResponse<ItemDto> getAllUserItems(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNumber,
                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
                                                 @RequestParam(value = "by", defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
                                                 @RequestParam(value = "sort", defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false)String sortDir)
    {
        return itemService.getAllUserItems(pageNumber,pageSize,sortBy,sortDir);
    }

    @PutMapping("/pass")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Change user password")
    @ApiParam(value = "oldPassword, newPassword")
    public ResponseEntity changeUserPassword(@RequestBody Map<String, String> passwords) throws Exception {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");
        userService.changeUserPassword(oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }
}
