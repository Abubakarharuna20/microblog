package com.example.microblog.controller;

import com.example.microblog.model.Comment;
import com.example.microblog.model.FileUploadUtil;
import com.example.microblog.model.Post;
import com.example.microblog.model.User;
import com.example.microblog.service.PostService;
import com.example.microblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User oauthUser = userService.User(user.getUsername(), user.getPassword());

        if (Objects.nonNull(oauthUser)) {
            return ResponseEntity.ok()
                    .body(Map.of("status", "success", "data", oauthUser)); // Include user data in the response
        } else {
            return ResponseEntity.ok()
                    .body(Map.of("status", "error", "message", "Incorrect username/password")); // Return appropriate response
        }
    }

    @PostMapping("/api/signup")
    public ResponseEntity<Object> signup(@RequestParam(value = "photo", required = false) MultipartFile multipartFile,
                                         @RequestParam("username") String username,
                                         @RequestParam("name") String name,
                                         @RequestParam("password") String password) throws IOException {
        // Check if the username already exists
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", "Username already exists")
            );
        }

        // Save the user's photo to the file system
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        // Create a new user object
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhoto(fileName);

        // Save the user to the database
        User savedUser = userService.saveUser(user);

        String uploadDir = "static/assets/uploads/users/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("status", "success", "message", "User signed up successfully")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(HttpStatus.FOUND).body("redirect:/login"); // Assuming redirect path
    }

    @GetMapping("/api/users")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/users/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        List<User> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/api/user/{userId}/posts")
    public List<Post> getUserPosts(@PathVariable Long userId){
        return userService.getUserPosts(userId);
    }

}