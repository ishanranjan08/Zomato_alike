package com.first.foodo.first_foodo.Controllers;


import com.first.foodo.first_foodo.Dto.UserDto;
import com.first.foodo.first_foodo.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private  UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


//    To create user
    @PostMapping
    public ResponseEntity<UserDto> createUsers(@Valid @RequestBody UserDto userDto){
        UserDto saveUser = userService.saveUser(userDto);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }


//    To get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok (userService.getAll());
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findById(@PathVariable("userId") String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }




}
