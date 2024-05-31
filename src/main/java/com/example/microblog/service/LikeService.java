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
import java.util.stream.Collectors;

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

    public List<Post> getPostsLikedByUser(Long userId) {
        List<Like> likes = likeRepository.findByUserId(userId);  // Fetch likes by user ID
        return likes.stream()
                .map(Like::getPost)  // Extract posts from likes
                .collect(Collectors.toList());
    }


    public void likePost(Long userId, Long postId) {
        // Check if the post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));


        // Create a new like
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

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

