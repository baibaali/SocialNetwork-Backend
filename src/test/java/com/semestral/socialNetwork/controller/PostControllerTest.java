package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.repository.UserRepository;
import com.semestral.socialNetwork.service.PostService;
import com.semestral.socialNetwork.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.PostModelWithoutUsersList;
import com.semestral.socialNetwork.model.UserModel;
import com.semestral.socialNetwork.model.UserModelWithoutPostsList;
import com.semestral.socialNetwork.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostService postService;

    @Test
    void createPost() throws Exception {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);
        given(postService.createPost(any(Post.class), any(Long.class))).willReturn(postModel);

        this.mockMvc.perform(
                    post("/posts", post)
                    .param("user_id", Long.toString(user.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\":\"some title\", \"body\":\"some body\"}")
        ).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(Integer.valueOf(Long.toString(postModel.getId())))))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(postModel.getTitle())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.body", CoreMatchers.is(postModel.getBody())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.postedAt", CoreMatchers.is(postModel.getPostedAt())));
    }

    @Test
    void createPostFailed() throws Exception {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);
        given(postService.createPost(any(Post.class), any(Long.class))).willThrow(UserDoesntExistsException.class);

        this.mockMvc.perform(
                        post("/posts", post)
                        .param("user_id", Long.toString(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"some title\", \"body\":\"some body\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void setLike() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);

        given(postService.setLike(user.getId(), postModel.getId())).willReturn(postModel.getId());

        this.mockMvc.perform(
                post("/posts/{id_post}", postModel.getId())
                .param("id_user", Long.toString(user.getId()))
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(Long.toString(postModel.getId()))));
    }

    @Test
    void setLikeFailed() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        PostModelWithoutUsersList postModel = PostModelWithoutUsersList.toModel(post);

        given(postService.setLike(any(Long.class), eq(postModel.getId()))).willThrow(UserDoesntExistsException.class);
        this.mockMvc.perform(
                        post("/posts/{id_post}", postModel.getId())
                        .param("id_user", Long.toString(2L))
        ).andExpect(status().is4xxClientError());

        Mockito.verify(postService, Mockito.atLeastOnce()).setLike(2L, postModel.getId());

        given(postService.setLike(eq(user.getId()), any(Long.class))).willThrow(PostDoesntExistsException.class);
        this.mockMvc.perform(
                post("/posts/{id_post}", 2L)
                .param("id_user", Long.toString(user.getId()))
        ).andExpect(status().is4xxClientError());

        Mockito.verify(postService, Mockito.atLeastOnce()).setLike(user.getId(), 2L);

    }

    @Test
    void getUsersWhoLikedPost() throws Exception {
        List<UserModelWithoutPostsList> usersWhoLiked = new ArrayList<>();
        User user = new User(1L, "baibaali", "dummypass");
        Post post = new Post(1L, "MyTitle", "My Body", user);

        User user1 = new User(2L, "User_1", "us1us");
        User user2 = new User(3L, "User_2", "2us2");

        usersWhoLiked.add(UserModelWithoutPostsList.toModel(user1));
        usersWhoLiked.add(UserModelWithoutPostsList.toModel(user2));

        given(postService.getUsersWhoLiked(post.getId())).willReturn(usersWhoLiked);

        this.mockMvc.perform(
                get("/posts/{id_post}/liked", post.getId())
        ).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(Integer.valueOf(Long.toString(usersWhoLiked.get(0).getId())))))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", CoreMatchers.is(usersWhoLiked.get(0).getUsername())))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(Integer.valueOf(Long.toString(usersWhoLiked.get(1).getId())))))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].username", CoreMatchers.is(usersWhoLiked.get(1).getUsername())));

        Mockito.verify(postService, Mockito.atLeastOnce()).getUsersWhoLiked(post.getId());
    }

    @Test
    void getUsersWhoLikedPostFailed() throws Exception {

        given(postService.getUsersWhoLiked(any(Long.class))).willThrow(PostDoesntExistsException.class);

        this.mockMvc.perform(
             get("/posts/{id_post}/liked", 1L)
        )
        .andExpect(status().is4xxClientError());
        Mockito.verify(postService, Mockito.atLeastOnce()).getUsersWhoLiked(1L);
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(
                delete("/posts/{id}", 1)
        ).andExpect(status().isOk());

        Mockito.verify(postService, Mockito.atLeastOnce()).deletePost(1L);
    }

    @Test
    void deletePostFailed() throws Exception {

        given(postService.deletePost(any(Long.class))).willThrow(PostDoesntExistsException.class);

        mockMvc.perform(
                delete("/posts/{id}", 1)
        ).andExpect(status().is4xxClientError());

        Mockito.verify(postService, Mockito.atLeastOnce()).deletePost(1L);
    }

    @Test
    void updatePost() throws Exception {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        PostModelWithoutUsersList updatedPost = PostModelWithoutUsersList.toModel(post);

        given(postService.updatePost(any(Long.class), any(Post.class))).willReturn(updatedPost);

        this.mockMvc.perform(
                        put("/posts/{id}/edit", updatedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"some title\", \"body\":\"some body\"}")
        ).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(Integer.valueOf(Long.toString(updatedPost.getId())))))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(updatedPost.getTitle())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.body", CoreMatchers.is(updatedPost.getBody())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.postedAt", CoreMatchers.is(updatedPost.getPostedAt())));
    }

    @Test
    void updatePostFailed() throws Exception {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        PostModelWithoutUsersList updatedPost = PostModelWithoutUsersList.toModel(post);

        given(postService.updatePost(any(Long.class), any(Post.class))).willThrow(PostDoesntExistsException.class);

        this.mockMvc.perform(
                put("/posts/{id}/edit", updatedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"some title\", \"body\":\"some body\"}")
        ).andExpect(status().is4xxClientError());
    }
}