package com.example.microblog.service;

import com.example.microblog.model.*;
import com.example.microblog.repository.FollowRepository;
import com.example.microblog.repository.FollowerRepository;
import com.example.microblog.repository.PostRepository;
import com.example.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PostRepository postRepository;


    public User User(String username, String password) {
        // Implement your logic to fetch the user from the database based on username and password
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User findByUsername(String username) {
        // Implement your logic to fetch the user from the database based on username
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        // Implement your logic to save the user to the database
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    public List<User> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Follow> follows = followRepository.findByFollowingId(userId);
        return follows.stream()
                .map(follow -> userRepository.findById(follow.getFollowerId())
                        .orElseThrow(() -> new IllegalArgumentException("Follower not found")))
                .collect(Collectors.toList());
    }

    public List<Post> getUserPosts(Long userId){
        return postRepository.findPostByUser_Id(userId);
    }


}

//import com.example.blog.model.User;
//import com.example.blog.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    public User User(String username, String password) {
//        return userRepository.findByUsernameAndPassword(username, password);
//    }
//
//    public User saveUser(User user) {
//        userRepository.save(user);
//        return user;
//    }
//}
