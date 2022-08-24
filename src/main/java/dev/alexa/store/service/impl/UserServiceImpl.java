package dev.alexa.store.service.impl;

import dev.alexa.store.domain.User;
import dev.alexa.store.exception.ResourceNotFoundException;
import dev.alexa.store.payload.ContentListResponse;
import dev.alexa.store.payload.CurrentUser;
import dev.alexa.store.payload.UserDto;
import dev.alexa.store.repository.UserRepository;
import dev.alexa.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override

    public ContentListResponse<UserDto> getListOfUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listOfUsers = page.getContent();

        List<UserDto> content = listOfUsers.stream().map(user -> mapToDto(user)).collect(Collectors.toList());

        ContentListResponse<UserDto> listResponse = new ContentListResponse<>();
        listResponse.setContent(content);
        listResponse.setLast(page.isLast());
        listResponse.setPageNumber(page.getNumber());
        listResponse.setPageSize(page.getSize());
        listResponse.setTotalElements(page.getTotalElements());
        listResponse.setTotalPages(page.getTotalPages());

        return listResponse;


    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto user = mapToDto(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id.toString())));
        return user;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setLastName(userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        if(user.getId() != id) {
            user.setRoles(userDto.getRoles());
        }
        UserDto updUser = mapToDto(userRepository.save(user));
        return updUser;

    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user", "id", id.toString()));
        userRepository.delete(user);

    }

    @Override
    public void changeUserPassword(String oldPassword, String newPassword) throws Exception {
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
        {
            throw new Exception("Old Password don't match");
        }
        User userFromDb= userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", user.getId().toString()));
        userFromDb.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userFromDb);
    }

    private UserDto mapToDto(User user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }

    private User mapToUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        return user;
    }
}
