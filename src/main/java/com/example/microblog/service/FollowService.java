package com.example.microblog.service;

import com.example.microblog.model.Follow;
import com.example.microblog.model.User;
import com.example.microblog.repository.FollowRepository;
import com.example.microblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;


    public void followUser(Long followerId, Long followingId) {
        Follow follow = new Follow(followerId, followingId);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }


    // Other methods for getting followers/following of a user, etc.
    public List<User> getFollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowingId(userId);
        return follows.stream()
                .map(follow -> userRepository.findById(follow.getFollowerId()).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }

    public List<User> getFollowing(Long userId) {
        List<Follow> follows = followRepository.findByFollowerId(userId);
        return follows.stream()
                .map(follow -> userRepository.findById(follow.getFollowingId()).orElse(null))
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }
}
