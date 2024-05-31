package com.example.microblog.controller;

import com.example.microblog.model.Comment;
import com.example.microblog.model.FileUploadUtil;
import com.example.microblog.model.Post;
import com.example.microblog.model.User;
import com.example.microblog.service.FollowService;
import com.example.microblog.service.PostService;
import com.example.microblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

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

        // Encrypt the password using SHA-256
        String encryptedPassword = encryptPassword(password);

        // Save the user's photo to the file system
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        // Create a new user object
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(encryptedPassword);  // Set the encrypted password
        user.setPhoto(fileName);

        // Save the user to the database
        User savedUser = userService.saveUser(user);

        String uploadDir = "src/main/resources/static/assets/uploads/posts/";
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("status", "success", "message", "User signed up successfully")
        );
    }

    public String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
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

    @PutMapping("/api/user/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long userId,
            @RequestParam(value = "photo", required = false) MultipartFile multipartFile,
            @RequestParam("username") String username,
            @RequestParam("name") String name,
            @RequestParam("bio") String bio) throws IOException {

        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Update user details
        existingUser.setName(name);
        existingUser.setUsername(username);
        existingUser.setBio(bio);

        if (multipartFile != null && !multipartFile.isEmpty()) {
            // Delete the previous photo if it exists
            if (existingUser.getPhoto() != null && !existingUser.getPhoto().isEmpty()) {
                String oldPhotoPath = "src/main/resources/static/assets/uploads/users/" + existingUser.getPhoto();
                File oldPhotoFile = new File(oldPhotoPath);
                if (oldPhotoFile.exists()) {
                    oldPhotoFile.delete();
                }
            }

            // Save the new user's photo to the file system
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            existingUser.setPhoto(fileName);

            String uploadDir = "src/main/resources/static/assets/uploads/users/";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        // Save the updated user to the database
        userService.saveUser(existingUser);

        return ResponseEntity.ok(Map.of("status", "success", "message", "User updated successfully", "data", existingUser));
    }

    @PutMapping("/api/user/{userId}/change-password")
    public ResponseEntity<Object> changePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwordMap) {

        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");
        String confirmPassword = passwordMap.get("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "New passwords do not match"));
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "error", "message", "User not found"));
        }

        if (!userService.checkPassword(user, oldPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "error", "message", "Old password is incorrect"));
        }

        userService.updatePassword(user, newPassword);

        return ResponseEntity.ok(Map.of("status", "success", "message", "Password changed successfully"));
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


    @GetMapping("/api/user/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/api/user/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        List<User> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/api/user/{userId}/posts")
    public List<Post> getUserPosts(@PathVariable Long userId){
        return userService.getUserPosts(userId);
    }

}