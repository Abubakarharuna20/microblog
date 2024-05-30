package com.example.microblog.controller;

import com.example.microblog.model.User;
import com.example.microblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

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

//    @GetMapping("/profile")
//    public String viewProfilePage() {
//        return "profile"; // Return your profile page here
//    }
    @GetMapping("/profile/@{username}")
    public String viewProfilePage(@PathVariable String username, Model model) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            // Handle user not found, maybe redirect to an error page or return 404
            return "error/404";
        }
        model.addAttribute("user", user);
        return "profile"; // Return your profile page here
    }

}