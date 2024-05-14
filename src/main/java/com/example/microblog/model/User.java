package com.example.microblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = true, length = 64)
    private String photo;

    @Transient
    public String getPhotoImagePath() {
        if (photo == null || id == null) return null;

        return "/user-photos/" + id + "/" + photo;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Post> posts = new ArrayList<>();

}
