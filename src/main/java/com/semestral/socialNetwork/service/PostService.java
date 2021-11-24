package com.semestral.socialNetwork.service;


import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.PostModel;
import com.semestral.socialNetwork.model.UserModel;
import com.semestral.socialNetwork.model.UserModelWithoutPostsList;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.NoSuchElementException;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PostModel createPost(Post post, Long user_id) throws UserDoesntExistsException {
        User user;
        if (userRepository.findById(user_id).isPresent()){
            user = userRepository.findById(user_id).get();
            post.setUser(user);
            post.setPostedAt();
            return PostModel.toModel(postRepository.save(post));
        }
        else
            throw new UserDoesntExistsException("There is no user with specified id");
    }

    public Long setLike(Long id_user, Long id_post) throws UserDoesntExistsException, PostDoesntExistsException {
        User user;
        Post post;
        try {
            user = userRepository.findById(id_user).get();
        } catch (NoSuchElementException e) {
            user = null;
        }

        try {
            post = postRepository.findById(id_post).get();
        } catch (NoSuchElementException e) {
            post = null;
        }

        if (user == null)
            throw new UserDoesntExistsException("There is no user with specified id");
        if (post == null)
            throw new PostDoesntExistsException("There is no post with specified id");

        post.addLike(user);
        postRepository.save(post);
        user.addLikedPost(post);
        userRepository.save(user);
        return id_post;
    }

    public List<UserModelWithoutPostsList> getUsersWhoLiked(Long id_post) throws PostDoesntExistsException {
        Post post;
        try {
            post = postRepository.findById(id_post).get();
        } catch (NoSuchElementException e) {
            post = null;
        }
        if (post == null)
            throw new PostDoesntExistsException("There is no post with specified id");
        return PostModel.toModel(post).getUsersWhoLiked();
    }


}
