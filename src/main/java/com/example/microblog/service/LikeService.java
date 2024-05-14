package com.example.microblog.service;

import com.example.microblog.model.Comment;
import com.example.microblog.model.Like;
import com.example.microblog.model.Post;
import com.example.microblog.model.User;
import com.example.microblog.repository.LikeRepository;
import com.example.microblog.repository.PostRepository;
import com.example.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Like> getLikesByPostId(Long postId){
        return likeRepository.findByPostId(postId);
    }


    public void likePost(Long userId, Long postId) {
        // Check if the post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        // Check if the user has already liked the post
//        boolean alreadyLiked = likeRepository.existsByUserIdAndPostId(userId, postId);
//        if (alreadyLiked) {
//            throw new RuntimeException("User already liked this post");
//        }

        // Create a new like
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        // Assuming you have a User entity and can set the user for the like
        // like.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId)));

        likeRepository.save(like);
    }

    public void unlikePost(Long userId, Long postId) {
        // Find and delete the like
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("Like not found for user " + userId + " and post " + postId));

        likeRepository.delete(like);
    }

    // Other methods for getting likes by post, user, etc.
}

