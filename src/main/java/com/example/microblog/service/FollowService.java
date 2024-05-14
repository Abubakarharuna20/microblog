package com.example.microblog.service;

import com.example.microblog.model.Follower;
import com.example.microblog.model.User;
import com.example.microblog.repository.FollowerRepository;
import com.example.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class FollowService {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private UserRepository userRepository;

    public void followUser(Long followerId, Long followingId) {
        // Check if the follow relationship already exists
        if (!followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            User follower = userRepository.findById(followerId)
                    .orElseThrow(() -> new RuntimeException("Follower not found with id: " + followerId));

            User following = userRepository.findById(followingId)
                    .orElseThrow(() -> new RuntimeException("Following user not found with id: " + followingId));

            Follower newFollower = new Follower();
            newFollower.setFollower(follower);
            newFollower.setFollowing(following);

            followerRepository.save(newFollower);
        }
    }

    public void unfollowUser(Long followerId, Long followingId) {
        // Check if the follow relationship exists
        Follower follower = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (follower != null) {
            followerRepository.delete(follower);
        }
    }

    // Other methods for getting followers/following of a user, etc.
}
