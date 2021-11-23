package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.UserModel;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

}
