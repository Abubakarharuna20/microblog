package com.example.microblog.repository;

import com.example.microblog.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // You can define custom methods for like-related operations if needed
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    List<Like> findByPostId(Long postId);

    List<Like> findByUserId(Long userId);

}
