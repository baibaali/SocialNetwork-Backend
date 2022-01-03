package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.PostModelWithoutUsersList;
import com.semestral.socialNetwork.model.UserModel;
import com.semestral.socialNetwork.model.UserModelWithoutPostsList;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;

import java.util.List;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PostRepository postRepository;

    @Test
    void registration() throws UserAlreadyExistsException {
        User user = new User(1L, "baibaali", "passwd");

        given(userRepository.save(any(User.class))).willReturn(user);

        User savedUser = userService.registration(user);

        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertEquals(user.getPassword(), savedUser.getPassword());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(argumentCaptor.capture());

        User userProvidedToSave = argumentCaptor.getValue();
        Assertions.assertEquals(1, userProvidedToSave.getId());
        Assertions.assertEquals("baibaali", userProvidedToSave.getUsername());
        Assertions.assertEquals("passwd", userProvidedToSave.getPassword());

    }

    @Test
    void getOneUser() throws UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");
        UserModel userModel = new UserModel(1L, "baibaali");

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));

        UserModel findUser = userService.getOneUser(userModel.getId());

        Assertions.assertEquals(userModel.getId(), findUser.getId());
        Assertions.assertEquals(userModel.getUsername(), findUser.getUsername());

       }

    @Test
    void deleteUser() throws UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");

        given(userRepository.existsById(1L)).willReturn(true);
        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(postRepository.findAll()).willReturn(new ArrayList<>());

        Long deletedUserId = userService.deleteUser(user.getId());
        Mockito.verify(userRepository, Mockito.atLeastOnce()).deleteById(user.getId());

        Assertions.assertEquals(user.getId(), deletedUserId);

    }

    @Test
    void getLikedPosts() throws UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");

        Post post = new Post(1L, "post title", "post body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);
        user.addLikedPost(post);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        List<PostModelWithoutUsersList> likedPosts = userService.getLikedPosts(user.getId());

        Mockito.verify(userRepository, Mockito.atLeastOnce()).findById(user.getId());

        Assertions.assertEquals(postModel.getId(), likedPosts.get(0).getId());
        Assertions.assertEquals(postModel.getTitle(), likedPosts.get(0).getTitle());
        Assertions.assertEquals(postModel.getBody(), likedPosts.get(0).getBody());
        Assertions.assertEquals(postModel.getPostedAt(), likedPosts.get(0).getPostedAt());

    }

    @Test
    void updateUser() throws UserAlreadyExistsException, UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");

        UserModelWithoutPostsList userModel = UserModelWithoutPostsList.toModel(user);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(userRepository.findByUsername("baibaali")).willReturn(user);
        given(userRepository.save(any(User.class))).willReturn(user);


        UserModelWithoutPostsList updatedUser = userService.updateUser(user.getId(), user);

        Assertions.assertEquals(userModel.getId(), updatedUser.getId());
        Assertions.assertEquals(userModel.getUsername(), updatedUser.getUsername());

        Mockito.verify(userRepository, atLeast(2)).findById(user.getId());
        Mockito.verify(userRepository, atMost(1)).findByUsername(user.getUsername());
        Mockito.verify(userRepository, atLeast(1)).save(user);


    }
}