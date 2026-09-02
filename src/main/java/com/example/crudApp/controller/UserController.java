package com.example.crudApp.controller;

import com.example.crudApp.model.User;
import com.example.crudApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    public UserService userService;
    @PostMapping("/register")
    public User register(@RequestBody User user)
    {
        return userService.addUser(user);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user)
    {
        return userService.verify(user);
    }
}
