package com.example.microblog.controller;

import com.example.microblog.model.User;
import com.example.microblog.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.followUser(followerId, followingId);
        return ResponseEntity.status(HttpStatus.CREATED).body("User followed successfully");
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestParam Long followerId, @RequestParam Long followingId) {
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok("User unfollowed successfully");
    }

    // Other endpoints for getting followers/following of a user, etc.
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        List<User> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }
}

