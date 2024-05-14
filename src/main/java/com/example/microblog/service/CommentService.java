package com.example.microblog.service;

import com.example.microblog.model.Comment;
import com.example.microblog.model.Post;
import com.example.microblog.model.User;
import com.example.microblog.repository.CommentRepository;
import com.example.microblog.repository.PostRepository;
import com.example.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;



    //    addComment()
    public Comment addComment(String content, Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException(postId + "This post id doesn't exists"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(userId + "This user id doesn't exists"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    public Comment getCommentByCommentId(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "This id doesn't exists"));
    }

    public List<Comment> getCommentByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }

    public void updateCommentByCommentId(Long commentId, Long postId, Comment comment){
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException(postId + "This post id doesn't exists"));
        comment.setId(commentId);
        comment.setPost(post);

        commentRepository.save(comment);
    }

    public void deleteCommentByCommentId(Long id){
        if(commentRepository.findById(id).isPresent()){
            commentRepository.deleteById(id);
        }else{
            throw new RuntimeException(id + " -> This is doesn't not exist");
        }
    }
}
