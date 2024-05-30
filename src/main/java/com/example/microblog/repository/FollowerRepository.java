package com.example.microblog.repository;

import com.example.microblog.model.Follower;
import com.example.microblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    // You can define custom methods for follower-related operations if needed
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<Follower> findByFollowing(User following);
    Follower findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
