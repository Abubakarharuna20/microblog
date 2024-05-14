package com.example.microblog.controller;

import com.example.microblog.model.Comment;
import com.example.microblog.model.Like;
import com.example.microblog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/post/{postId}")
    public List<Like> getLikesByPostId(@PathVariable Long postId){
        return likeService.getLikesByPostId(postId);
    }

    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        likeService.likePost(userId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post liked successfully");
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<String> unlikePost(@RequestParam("userId") Long userId, @RequestParam("postId") Long postId) {
        likeService.unlikePost(userId, postId);
        return ResponseEntity.ok("Post unliked successfully");
    }

    // Other endpoints for getting likes by post, user, etc.
}


