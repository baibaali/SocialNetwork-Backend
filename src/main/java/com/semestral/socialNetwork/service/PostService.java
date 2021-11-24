package com.semestral.socialNetwork.service;


import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.PostModel;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
