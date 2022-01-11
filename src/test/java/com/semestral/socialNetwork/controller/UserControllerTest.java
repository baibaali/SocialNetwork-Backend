package com.semestral.socialNetwork.controller;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void registration() throws Exception {
        User user = new User(1, "baibaali", "dummypass");

        given(userService.registration(any(User.class))).willReturn(user);

        this.mockMvc.perform(
                        post("/users", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"baibaali\", \"password\":\"dummypass\"}")
                ).andExpect(status().isOk())
                 .andExpect(content().string(containsString("User was successfully created")));
    }

    @Test
    public void registrationFailed() throws Exception {
        User user = new User(1, "baibaali", "dummypass");

        given(userService.registration(any(User.class))).willReturn(user);
        this.mockMvc.perform(
                        post("/users", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"baibaali\", \"password\":\"dummypass\"}")
        ).andExpect(status().isOk())
         .andExpect(content().string(containsString("User was successfully created")));


        given(userService.registration(any(User.class))).willThrow(UserAlreadyExistsException.class);
        this.mockMvc.perform(
                        post("/users", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"baibaali\", \"password\":\"dummypass\"}")
        ).andExpect(status().is4xxClientError());
        Mockito.verify(userService, Mockito.atLeast(2)).registration(any(User.class));
    }

    @Test
    void getOneUser() throws Exception {
        UserModel user = new UserModel(1, "baibaali");

        given(userService.getOneUser(user.getId())).willReturn(user);
        this.mockMvc.perform(
                get("/users")
                .param("id", Long.toString(user.getId()))
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(Integer.valueOf(Long.toString(user.getId())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.posts", CoreMatchers.is(user.getPosts())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likedPosts", CoreMatchers.is(user.getLikedPosts())));
        Mockito.verify(userService, Mockito.atLeastOnce()).getOneUser(user.getId());
    }

    @Test
    void getOneUserFailed() throws Exception {
        UserModel user = new UserModel(1, "baibaali");

        given(userService.getOneUser(user.getId())).willThrow(UserDoesntExistsException.class);
        this.mockMvc.perform(
                get("/users")
                .param("id", Long.toString(user.getId()))
        ).andExpect(status().is4xxClientError());
        Mockito.verify(userService, Mockito.atLeastOnce()).getOneUser(user.getId());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(
                delete("/users/{id}", 1)
        ).andExpect(status().isOk());

        Mockito.verify(userService, Mockito.atLeastOnce()).deleteUser(1L);
    }

    @Test
    void deleteUserFailed() throws Exception {

        given(userService.deleteUser(1L)).willThrow(UserDoesntExistsException.class);

        mockMvc.perform(
                delete("/users/{id}", 1)
        ).andExpect(status().is4xxClientError());

        Mockito.verify(userService, Mockito.atLeastOnce()).deleteUser(1L);
    }

    @Test
    void getLikedPost() throws Exception {
        List<PostModelWithoutUsersList> likedPosts = new ArrayList<>();
        User user = new User(1L, "baibaali", "dummypass");
        Post post1 = new Post(1L, "Title", "Body", user);
        Post post2 = new Post(2L, "Title 2", "Body 2", user);

        likedPosts.add(PostModelWithoutUsersList.toModel(post1));
        likedPosts.add(PostModelWithoutUsersList.toModel(post2));

        given(userService.getLikedPosts(user.getId())).willReturn(likedPosts);

        this.mockMvc.perform(
                get("/users/{id_user}/liked_posts", user.getId())
        )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(Integer.valueOf(Long.toString(likedPosts.get(0).getId())))))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", CoreMatchers.is(likedPosts.get(0).getTitle())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].body", CoreMatchers.is(likedPosts.get(0).getBody()))).andExpect(MockMvcResultMatchers.jsonPath("$[0].body", CoreMatchers.is(likedPosts.get(0).getBody())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].postedAt", CoreMatchers.is(likedPosts.get(0).getPostedAt())))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(Integer.valueOf(Long.toString(likedPosts.get(1).getId())))))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", CoreMatchers.is(likedPosts.get(1).getTitle())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].body", CoreMatchers.is(likedPosts.get(1).getBody())))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].postedAt", CoreMatchers.is(likedPosts.get(1).getPostedAt())));

        Mockito.verify(userService, Mockito.atLeastOnce()).getLikedPosts(user.getId());

    }

    @Test
    void getLikedPostFailed() throws Exception {

        given(userService.getLikedPosts(any(Long.class))).willThrow(UserDoesntExistsException.class);

        this.mockMvc.perform(
                        get("/users/{id_user}/liked_posts", 1L)
        ).andExpect(status().is4xxClientError());

        Mockito.verify(userService, Mockito.atLeastOnce()).getLikedPosts(1L);
    }

    @Test
    void updateUser() throws Exception {
        User user = new User (1, "baibaali", "dummypass");
        UserModelWithoutPostsList updatedUser = UserModelWithoutPostsList.toModel(user);

        given(userService.updateUser(any(Long.class), any(User.class))).willReturn(updatedUser);

        this.mockMvc.perform(
                put("/users/{id}/edit", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"baibaali\", \"password\":\"dummypass\"}")
        ).andExpect(status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(user.getUsername())));
    }

    @Test
    void updateUserFailed() throws Exception {

        given(userService.updateUser(any(Long.class), any(User.class))).willThrow(UserAlreadyExistsException.class);
        this.mockMvc.perform(
                        put("/users/{id}/edit", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"baibaali\", \"password\":\"dummypass\"}")
                ).andExpect(status().is4xxClientError());

        given(userService.updateUser(any(Long.class), any(User.class))).willThrow(UserDoesntExistsException.class);
        this.mockMvc.perform(
                put("/users/{id}/edit", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"baibaali\", \"password\":\"dummypass\"}")
        ).andExpect(status().is4xxClientError());

    }
}