package com.semestral.socialNetwork.service;


import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.dto.PostDTO;
import com.semestral.socialNetwork.dto.PostDTOWithoutUsersList;
import com.semestral.socialNetwork.dto.UserDTOWithoutPostsList;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PostDTOWithoutUsersList createPost(Post post, Long user_id) throws UserDoesntExistsException {
        User user;
        if (userRepository.findById(user_id).isPresent()){
            user = userRepository.findById(user_id).get();
            post.setUser(user);
            post.setPostedAt();
            return PostDTOWithoutUsersList.toModel(postRepository.save(post));
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

    public List<UserDTOWithoutPostsList> getUsersWhoLiked(Long id_post) throws PostDoesntExistsException {
        Post post;
        try {
            post = postRepository.findById(id_post).get();
        } catch (NoSuchElementException e) {
            post = null;
        }
        if (post == null)
            throw new PostDoesntExistsException("There is no post with specified id");
        return PostDTO.toModel(post).getUsersWhoLiked();
    }

    public Long deletePost(Long id) throws PostDoesntExistsException {
        if (postRepository.existsById(id)) {
            Post post = postRepository.findById(id).get();
            for(User user: userRepository.findAll())
                if(user.getLikedPosts().contains(post))
                    user.getLikedPosts().remove(post);
            postRepository.deleteById(id);
            return id;
        }
        else
            throw new PostDoesntExistsException("There is no user with specified id");
    }

    public PostDTOWithoutUsersList updatePost(Long id, Post updatedPost) throws PostDoesntExistsException {
        if (postRepository.findById(id).isEmpty())
            throw new PostDoesntExistsException("There is no post with specified id");
        Post post = postRepository.findById(id).get();
        boolean isChanged = false;
        if (updatedPost.getTitle() != null && !Objects.equals(updatedPost.getTitle(), post.getTitle())) {
            post.setTitle(updatedPost.getTitle());
            isChanged = true;
        }
        if (updatedPost.getBody() != null && !Objects.equals(updatedPost.getBody(), post.getBody())) {
            post.setBody(updatedPost.getBody());
            isChanged = true;
        }
        if (isChanged)
            post.setPostedAt();

        return PostDTOWithoutUsersList.toModel(postRepository.save(post));

    }

}
