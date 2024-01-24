package com.example.userservice.controller;


import com.example.userservice.pojo.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        System.out.println(userService.getAllUsers());
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
//        System.out.println(truth);
        Optional<User> user = userService.getUserById(id);
        System.out.println(user);
        return user.orElse(null);
    }
}
