package com.example.microblog.service;

import com.example.microblog.model.Post;
import com.example.microblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post getPostById(Long id){
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "This id doesn't exists"));
    }

    public List<Post> getAllPost(){
        return postRepository.findAll();
    }

    public void deletePostById(Long id){
        if(postRepository.findById(id).isPresent()){
            postRepository.deleteById(id);
        }else{
            throw new RuntimeException(id + " -> This is doesn't not exist");
        }
    }
}
