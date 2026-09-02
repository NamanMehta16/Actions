package com.example.crudApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobControlller {
    @GetMapping("/jobs")
    public void getPosts()
    {

    }
}
