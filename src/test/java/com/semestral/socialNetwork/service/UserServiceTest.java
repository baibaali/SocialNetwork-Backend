package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.dto.PostDTOWithoutUsersList;
import com.semestral.socialNetwork.dto.UserDTO;
import com.semestral.socialNetwork.dto.UserDTOWithoutPostsList;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;

    @Test
    void registration() throws UserAlreadyExistsException {
        User user = new User(1L, "baibaali", "passwd");

        given(userRepository.save(any(User.class))).willReturn(user);

        User savedUser = userService.registration(user);

        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(argumentCaptor.capture());

        User userProvidedToSave = argumentCaptor.getValue();
        assertEquals(1, userProvidedToSave.getId());
        assertEquals("baibaali", userProvidedToSave.getUsername());
        assertEquals("passwd", userProvidedToSave.getPassword());

    }

    @Test
    void getOneUser() throws UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");
        UserDTO userDTO = new UserDTO(1L, "baibaali");

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));

        UserDTO findUser = userService.getOneUser(userDTO.getId());

        assertEquals(userDTO.getId(), findUser.getId());
        assertEquals(userDTO.getUsername(), findUser.getUsername());

       }

    @Test
    void deleteUser() throws UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");

        given(userRepository.existsById(1L)).willReturn(true);
        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(postRepository.findAll()).willReturn(new ArrayList<>());

        Long deletedUserId = userService.deleteUser(user.getId());
        Mockito.verify(userRepository, Mockito.atLeastOnce()).deleteById(user.getId());

        assertEquals(user.getId(), deletedUserId);

    }

    @Test
    void getLikedPosts() throws UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");

        Post post = new Post(1L, "post title", "post body", user);
        PostDTOWithoutUsersList postModel = PostDTOWithoutUsersList.toModel(post);
        user.addLikedPost(post);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        List<PostDTOWithoutUsersList> likedPosts = userService.getLikedPosts(user.getId());

        Mockito.verify(userRepository, Mockito.atLeastOnce()).findById(user.getId());

        assertEquals(postModel.getId(), likedPosts.get(0).getId());
        assertEquals(postModel.getTitle(), likedPosts.get(0).getTitle());
        assertEquals(postModel.getBody(), likedPosts.get(0).getBody());
        assertEquals(postModel.getPostedAt(), likedPosts.get(0).getPostedAt());

    }

    @Test
    void updateUser() throws UserAlreadyExistsException, UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");

        UserDTOWithoutPostsList userModel = UserDTOWithoutPostsList.toModel(user);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);


        UserDTOWithoutPostsList updatedUser = userService.updateUser(user.getId(), user);

        assertEquals(userModel.getId(), updatedUser.getId());
        assertEquals(userModel.getUsername(), updatedUser.getUsername());

        Mockito.verify(userRepository, atLeast(2)).findById(user.getId());
        Mockito.verify(userRepository, atMost(1)).findByUsername(user.getUsername());
        Mockito.verify(userRepository, atLeast(1)).save(user);


    }
}