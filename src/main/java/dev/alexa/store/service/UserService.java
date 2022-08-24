package dev.alexa.store.service;

import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.payload.UserDto;

import java.util.List;

public interface UserService {
    ContentListResponse<UserDto> getListOfUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    UserDto
    getUserById(Long id);
    UserDto updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);

    void changeUserPassword(String oldPassword, String newPassword) throws Exception;
}
