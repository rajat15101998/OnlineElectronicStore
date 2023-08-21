package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //getAllUsers
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //getSingleUserById
    UserDto getUserById(String userId);

    //getSingleUserByEMail
    UserDto getUserByEmail(String userEmail);

    //Search User
    List<UserDto> searchUser(String keyword);
}
