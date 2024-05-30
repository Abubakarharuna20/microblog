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
        return followRepository.findFollowersByUserId(userId);
    }

    public List<User> getFollowing(Long userId) {
        return followRepository.findFollowingByUserId(userId);
    }
}
