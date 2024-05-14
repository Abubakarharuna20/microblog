package com.example.microblog.controller;

import com.example.microblog.model.Comment;
import com.example.microblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/posts/{id}/comment")
    public ResponseEntity<String> createComment(@RequestBody Map<String, String> requestBody, @PathVariable Long id) {
        String content = requestBody.get("content");
        Long userId = Long.parseLong(requestBody.get("userId"));
        Comment response = commentService.addComment(content, userId, id);

        return new ResponseEntity<>("Comment created successfully for Id -> " + response.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/api/comment/{id}")
    public ResponseEntity<Comment> getCommentByCommentId(@PathVariable Long id){
        Comment comment = commentService.getCommentByCommentId(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/api/posts/{id}/comment")
    public List<Comment> getCommentByPostId(@PathVariable Long id){
        return commentService.getCommentByPostId(id);
    }

    @PutMapping("/api/posts/{postId}/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody Comment comment){
        commentService.updateCommentByCommentId(commentId, postId, comment);
        return new ResponseEntity<>("Comment updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id){
        commentService.deleteCommentByCommentId(id);

        return new ResponseEntity<>("Comment deleted successfully.", HttpStatus.OK);
    }

}
