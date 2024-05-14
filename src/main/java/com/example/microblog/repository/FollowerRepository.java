package com.example.microblog.repository;

import com.example.microblog.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    // You can define custom methods for follower-related operations if needed
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Follower findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
