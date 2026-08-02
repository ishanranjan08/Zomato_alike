package com.first.foodo.first_foodo.Service.impl;

import com.first.foodo.first_foodo.Config.AppConstants;
import com.first.foodo.first_foodo.Dto.UserDto;
import com.first.foodo.first_foodo.Entity.RoleEntity;
import com.first.foodo.first_foodo.Entity.User;
import com.first.foodo.first_foodo.Exception.ResourceNotFoundException;
import com.first.foodo.first_foodo.Repository.RoleRepo;
import com.first.foodo.first_foodo.Repository.UserRepo;
import com.first.foodo.first_foodo.Service.UserService;
import com.first.foodo.first_foodo.Utils.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private RoleRepo roleRepo;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
//        Generated new user id
        userDto.setId(Helper.generate());
        User user = convertUserDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

//        Save user in database by converting UserDto to user first
        // guest: role I
        RoleEntity roleGuest = roleRepo.findByName(AppConstants.getRoleGuest());
        if (roleGuest == null) {
            throw new ResourceNotFoundException(
                    "GUEST role is not set up in the database. Seed the role_entity table with '"
                            + AppConstants.getRoleGuest() + "' before creating users.");
        }
        user.getRoleEntities ().add(roleGuest);
        User savedUser = userRepo.save(user);
        return convertUsertoUserDto(savedUser);

    }

    @Override
    public UserDto update(UserDto userDto, String userId) {
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not Found"));

        // Update fields from DTO
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setAddress(userDto.getAddress());

        User updatedUser = userRepo.save(existingUser);

        return convertUsertoUserDto(updatedUser);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepo.findAll();
        List<UserDto> list = users.stream().
                map((user) -> convertUsertoUserDto(user)).
                toList();
        return list;
    }

    @Override
    public List<UserDto> getUserByName(String userName) {

        return userRepo.findByName(userName).stream().
                map((user)->convertUsertoUserDto(user)).toList();
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return convertUsertoUserDto(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return convertUsertoUserDto(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepo.delete(user);

    }


    private User convertUserDtoToUser (UserDto userDto) {
        return modelMapper.map(userDto,User.class);
    }

    private UserDto convertUsertoUserDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

}
