package com.example.microblog.repository;

import com.example.microblog.model.Follow;
import com.example.microblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    // You can define custom methods for follower-related operations if needed
    List<Follow> findByFollowerId(Long followerId);
    List<Follow> findByFollowingId(Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT u FROM User u JOIN Follow f ON u.id = f.followerId WHERE f.followingId = :userId")
    List<User> findFollowersByUserId(Long userId);

    @Query("SELECT u FROM User u JOIN Follow f ON u.id = f.followerId WHERE f.followingId = :userId")
    List<User> findFollowingByUserId(Long userId);
}
