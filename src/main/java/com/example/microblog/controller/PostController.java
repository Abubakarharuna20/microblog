package com.example.microblog.controller;

import com.example.microblog.model.FileUploadUtil;
import com.example.microblog.model.Post;
import com.example.microblog.model.User;
import com.example.microblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/uploads/posts/";

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/api/posts/create")
    public ResponseEntity<String> createPost(
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            String imageName = null;
            if (image != null && !image.isEmpty()) {
                // Get the file name
                imageName = image.getOriginalFilename();

                // Create the directory if it does not exist
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Save the file to the server
                Path filePath = Paths.get(UPLOAD_DIR + imageName);
                Files.write(filePath, image.getBytes());
            }

            Post post = new Post();
            User user = new User();
            user.setId(userId); // Assuming setId method exists in User class
            post.setUser(user);
            post.setContent(content);
            post.setImage(imageName);

            postService.createPost(post);

            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process image");
        }
    }


    @GetMapping("/api/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        Post response = postService.getPostById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/posts")
    public List<Post> getAllPost(){
        return postService.getAllPost();
    }

//    @PutMapping("/api/posts/{id}")
//    public ResponseEntity<String> updatePost(@RequestBody Post post, @PathVariable Long id){
//        postService.updatePostById(post, id);
//
//        return new ResponseEntity<>("Post updated successfully.", HttpStatus.OK);
//    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id){
        postService.deletePostById(id);

        return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
    }
}


