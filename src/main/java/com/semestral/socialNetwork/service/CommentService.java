package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.CommentModel;
import com.semestral.socialNetwork.repository.CommentRepository;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;


    public CommentModel writeComment(Long id_user, Long id_post, Comment comment) throws UserDoesntExistsException, PostDoesntExistsException {
        if (userRepository.findById(id_user).isEmpty())
            throw new UserDoesntExistsException("User with specified id is not found");
        if (postRepository.findById(id_post).isEmpty())
            throw new PostDoesntExistsException("Post with specified id is not found");
        comment.setPost(postRepository.findById(id_post).get());
        comment.setUser(userRepository.findById(id_user).get());
        postRepository.findById(id_post).get().setComment(comment);
        userRepository.findById(id_user).get().setComment(comment);
        return CommentModel.toModel(commentRepository.save(comment));
    }
}
