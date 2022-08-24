package dev.alexa.store.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.payload.UserDto;
import dev.alexa.store.security.View;
import dev.alexa.store.service.UserService;
import dev.alexa.store.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(View.UserView.FullInfo.class)
    public ResponseEntity<UserDto> getUserById(@PathVariable(name="id") Long id)
    {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView( View.ResponseView.MinimalView.class)
    public ContentListResponse<UserDto> getAllUsers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNumber,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false)int pageSize,
                                                    @RequestParam(value = "by", defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
                                                    @RequestParam(value = "sort", defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false)String sortDir)
    {
        return userService.getListOfUsers(pageNumber,pageSize,sortBy,sortDir);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable(name = "id") Long id)
    {
        UserDto updatedUser = userService.updateUser(userDto, id);
        return new ResponseEntity(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable(name = "id") Long id)
    {
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
