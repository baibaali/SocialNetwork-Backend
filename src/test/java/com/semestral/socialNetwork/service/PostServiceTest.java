package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.PostModelWithoutUsersList;
import com.semestral.socialNetwork.model.UserModelWithoutPostsList;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(PostService.class)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PostRepository postRepository;

    @Test
    void createPost() throws UserAlreadyExistsException, UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");
        Post post = new Post(1L, "post title", "post body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(postRepository.save(any(Post.class))).willReturn(post);

        PostModelWithoutUsersList savedPost = postService.createPost(post, user.getId());

        Assertions.assertEquals(postModel.getId(), savedPost.getId());
        Assertions.assertEquals(postModel.getTitle(), savedPost.getTitle());
        Assertions.assertEquals(postModel.getBody(), savedPost.getBody());
        Assertions.assertEquals(postModel.getPostedAt(), savedPost.getPostedAt());

        Mockito.verify(postRepository, Mockito.atLeastOnce()).save(post);

    }

    @Test
    void setLike() throws PostDoesntExistsException, UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");
        Post post = new Post(1L, "post title", "post body", user);

        user.addLikedPost(post);
        post.addLike(user);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(postRepository.findById(1L)).willReturn(java.util.Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);
        given(userRepository.save(any(User.class))).willReturn(user);

        Long likedPostId = postService.setLike(user.getId(), post.getId());

        Assertions.assertEquals(1L, likedPostId);

        Mockito.verify(postRepository, atLeastOnce()).save(post);
        Mockito.verify(userRepository, atLeastOnce()).save(user);
        Mockito.verify(postRepository, atLeastOnce()).findById(post.getId());
        Mockito.verify(userRepository, atLeastOnce()).findById(user.getId());

    }

    @Test
    void getUsersWhoLiked() throws PostDoesntExistsException {

        User user = new User(1L, "baibaali", "passwd");
        Post post = new Post(1L, "post title", "post body", user);
        UserModelWithoutPostsList userModel = UserModelWithoutPostsList.toModel(user);
        post.addLike(user);

        given(postRepository.findById(1L)).willReturn(java.util.Optional.of(post));

        List<UserModelWithoutPostsList> usersWhoLiked = postService.getUsersWhoLiked(post.getId());

        Assertions.assertEquals(userModel.getId(), usersWhoLiked.get(0).getId());
        Assertions.assertEquals(userModel.getUsername(), usersWhoLiked.get(0).getUsername());

        Mockito.verify(postRepository, atLeastOnce()).findById(post.getId());
    }

    @Test
    void deletePost() throws PostDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");
        Post post = new Post(1L, "post title", "post body", user);

        given(postRepository.existsById(1L)).willReturn(true);
        given(postRepository.findById(1L)).willReturn(java.util.Optional.of(post));
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        given(userRepository.findAll()).willReturn(allUsers);

        Long deletedPostId = postService.deletePost(post.getId());

        Assertions.assertEquals(1L, deletedPostId);

        Mockito.verify(postRepository, atLeastOnce()).deleteById(post.getId());
    }

    @Test
    void updatePost() throws PostDoesntExistsException {
        User user = new User(1L, "baibaali", "passwd");
        Post post = new Post(1L, "post title", "post body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);

        given(postRepository.findById(1L)).willReturn(java.util.Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);

        PostModelWithoutUsersList updatedPost = postService.updatePost(post.getId(), post);

        Assertions.assertEquals(postModel.getId(), updatedPost.getId());
        Assertions.assertEquals(postModel.getTitle(), updatedPost.getTitle());
        Assertions.assertEquals(postModel.getBody(), updatedPost.getBody());
        Assertions.assertEquals(postModel.getPostedAt(), updatedPost.getPostedAt());

        Mockito.verify(postRepository, atLeast(2)).findById(post.getId());
        Mockito.verify(postRepository, atLeastOnce()).save(post);

    }
}