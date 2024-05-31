package com.example.microblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT", name = "bio")
    private String bio;

    @Column(nullable = true, length = 64)
    private String photo;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Transient
    public String getPhotoImagePath() {
        if (photo == null || id == null) return null;
        return "/assets/uploads/users/" + photo;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Post> posts = new ArrayList<>();

    // Other fields, getters, and setters
    @OneToMany(mappedBy = "followerId")
    private List<Follow> following;

    @OneToMany(mappedBy = "followingId")
    private List<Follow> followers;

    // Getters and setters for these relationships

}
