package com.first.foodo.first_foodo.Service;

import com.first.foodo.first_foodo.Dto.UserDto;

import java.util.List;

public interface UserService {


    UserDto saveUser(UserDto user);
    UserDto update(UserDto user, String userId);

    List<UserDto> getAll();

    List<UserDto> getUserByName(String userName);


    UserDto getUserByEmail (String email);

    UserDto getUserById(String userId);


    void deleteUser (String userId);

}
