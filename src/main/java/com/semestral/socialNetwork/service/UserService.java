package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.PostModel;
import com.semestral.socialNetwork.model.PostModelWithoutUsersList;
import com.semestral.socialNetwork.model.UserModel;
import com.semestral.socialNetwork.model.UserModelWithoutPostsList;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public User registration(@RequestBody User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()) != null)
            throw new UserAlreadyExistsException("This username is already taken");
        return userRepository.save(user);
    }


    public UserModel getOneUser(Long id) throws UserDoesntExistsException {
        User user;
        try{
            user = userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            user = null;
        }
        if(user == null)
            throw new UserDoesntExistsException("There is no user with specified id");
        return UserModel.toModel(user);
    }

    public Long deleteUser(Long id) throws UserDoesntExistsException {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();
            for(Post post: postRepository.findAll())
                if(post.getUsersWhoLiked().contains(user))
                    post.getUsersWhoLiked().remove(user);
            userRepository.deleteById(id);
            return id;
        }
        else
            throw new UserDoesntExistsException("There is no user with specified id");
    }

    public List<PostModelWithoutUsersList> getLikedPosts(Long id_user) throws UserDoesntExistsException {
        User user;
        try {
            user = userRepository.findById(id_user).get();
        } catch (NoSuchElementException e) {
            user = null;
        }
        if (user == null)
            throw new UserDoesntExistsException("There is no user with specified id");
        return UserModel.toModel(user).getLikedPosts();
    }

    public UserModelWithoutPostsList updateUser(Long id, User updatedUser) throws UserDoesntExistsException, UserAlreadyExistsException {
        if (userRepository.findById(id).isEmpty())
            throw new UserDoesntExistsException("There is no user with specified id");
        User user = userRepository.findById(id).get();
        if (updatedUser.getUsername() != null && !Objects.equals(updatedUser.getUsername(), user.getUsername())) {
            if(userRepository.findByUsername(updatedUser.getUsername()) != null)
                throw new UserAlreadyExistsException("This username is already taken");
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }

        return UserModelWithoutPostsList.toModel(userRepository.save(user));

    }

}
