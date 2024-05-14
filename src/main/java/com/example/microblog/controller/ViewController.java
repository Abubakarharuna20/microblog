package com.example.microblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ViewController {

    // User Routes
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Return your login page here
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Return your signup page here
    }

    @GetMapping("/post/{postId}")
    public String viewSinglePost() {
        return "viewPost"; // Return the name of the Thymeleaf template
    }
}