package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.exception.BadPermissionException;
import com.semestral.socialNetwork.exception.CommentDoesntExistsException;
import com.semestral.socialNetwork.dto.CommentDTO;
import com.semestral.socialNetwork.service.CommentService;
import org.junit.jupiter.api.Test;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import org.hamcrest.CoreMatchers;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void writeComment() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);
        CommentDTO commentDTO = CommentDTO.toModel(comment);

        given(commentService.writeComment(any(Long.class), any(Long.class), any(Comment.class))).willReturn(commentDTO);
        this.mockMvc.perform(
                post("/users/{id_user}/posts/{id_post}", user.getId(), post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(Integer.valueOf(Long.toString(commentDTO.getId())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentBody", CoreMatchers.is(commentDTO.getCommentBody())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentTime", CoreMatchers.is(commentDTO.getCommentTime())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentOwner.id", CoreMatchers.is(Integer.valueOf(Long.toString(commentDTO.getCommentOwner().getId())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentOwner.username", CoreMatchers.is(commentDTO.getCommentOwner().getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.id", CoreMatchers.is(Integer.valueOf(Long.toString(commentDTO.getPostThatWasCommented().getId())))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.title", CoreMatchers.is(commentDTO.getPostThatWasCommented().getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.body", CoreMatchers.is(commentDTO.getPostThatWasCommented().getBody())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.postedAt", CoreMatchers.is(commentDTO.getPostThatWasCommented().getPostedAt())));
    }

    @Test
    void writeCommentFailed() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);

        given(commentService.writeComment(any(Long.class), eq(post.getId()), any(Comment.class))).willThrow(UserDoesntExistsException.class);
        this.mockMvc.perform(
                    post("/users/{id_user}/posts/{id_post}", 2L, post.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());

        given(commentService.writeComment(eq(user.getId()), any(Long.class), any(Comment.class))).willThrow(PostDoesntExistsException.class);
        this.mockMvc.perform(
                post("/users/{id_user}/posts/{id_post}", user.getId(), 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());

    }

    @Test
    void deleteComment() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);

        given(commentService.deleteComment(user.getId(), post.getId(), comment.getId())).willReturn(comment.getId());
        this.mockMvc.perform(
                delete("/users/{id_user}/posts/{id_post}/comments/{id_comment}", user.getId(), post.getId(), comment.getId())
        ).andExpect(status().isOk())
                .andExpect(content().string(containsString(Long.toString(comment.getId()))));

        Mockito.verify(commentService, Mockito.atLeastOnce()).deleteComment(user.getId(), post.getId(), comment.getId());
    }

    @Test
    void deleteCommentFailed() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);

        given(commentService.deleteComment(any(Long.class), eq(post.getId()), eq(comment.getId()))).willThrow(UserDoesntExistsException.class);
        this.mockMvc.perform(
                        delete("/users/{id_user}/posts/{id_post}/comments/{id_comment}", 2L, post.getId(), comment.getId())
                ).andExpect(status().is4xxClientError());

        Mockito.verify(commentService, Mockito.atLeastOnce()).deleteComment(2L, post.getId(), comment.getId());

        given(commentService.deleteComment(eq(user.getId()), any(Long.class), eq(comment.getId()))).willThrow(PostDoesntExistsException.class);
        this.mockMvc.perform(
                delete("/users/{id_user}/posts/{id_post}/comments/{id_comment}", user.getId(), 2L, comment.getId())
        ).andExpect(status().is4xxClientError());

        Mockito.verify(commentService, Mockito.atLeastOnce()).deleteComment(user.getId(), 2L, comment.getId());

        given(commentService.deleteComment(eq(user.getId()), eq(post.getId()), any(Long.class))).willThrow(CommentDoesntExistsException.class);
        this.mockMvc.perform(
                delete("/users/{id_user}/posts/{id_post}/comments/{id_comment}", user.getId(), post.getId(), 2L)
        ).andExpect(status().is4xxClientError());

        Mockito.verify(commentService, Mockito.atLeastOnce()).deleteComment(user.getId(), post.getId(), 2L);


        User user2 = new User(2L, "baibaali2", "passw2");

        given(commentService.deleteComment(any(Long.class), eq(post.getId()), eq(comment.getId()))).willThrow(BadPermissionException.class);
        this.mockMvc.perform(
                delete("/users/{id_user}/posts/{id_post}/comments/{id_comment}", 2L, post.getId(), comment.getId())
        ).andExpect(status().is4xxClientError());

        Mockito.verify(commentService, Mockito.atLeastOnce()).deleteComment(2L, post.getId(), comment.getId());


        Post post2 = new Post(2L, "second post", "here we go again", user2);

        given(commentService.deleteComment(any(Long.class), any(Long.class), eq(comment.getId()))).willThrow(BadPermissionException.class);
        this.mockMvc.perform(
                delete("/users/{id_user}/posts/{id_post}/comments/{id_comment}", user2.getId(), post2.getId(), comment.getId())
        ).andExpect(status().is4xxClientError());

        Mockito.verify(commentService, Mockito.atLeastOnce()).deleteComment(user2.getId(), post2.getId(), comment.getId());

    }

    @Test
    void updateComment() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);
        CommentDTO commentDTO = CommentDTO.toModel(comment);

        given(commentService.updateComment(eq(user.getId()), eq(post.getId()), eq(comment.getId()), any(Comment.class))).willReturn(commentDTO);

        this.mockMvc.perform(
                    put("/users/{id_user}/posts/{id_post}/comments", user.getId(), post.getId())
                    .param("id_comment", Long.toString(commentDTO.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(Integer.valueOf(Long.toString(commentDTO.getId())))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.commentBody", CoreMatchers.is(commentDTO.getCommentBody())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.commentTime", CoreMatchers.is(commentDTO.getCommentTime())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.commentOwner.id", CoreMatchers.is(Integer.valueOf(Long.toString(commentDTO.getCommentOwner().getId())))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.commentOwner.username", CoreMatchers.is(commentDTO.getCommentOwner().getUsername())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.id", CoreMatchers.is(Integer.valueOf(Long.toString(commentDTO.getPostThatWasCommented().getId())))))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.title", CoreMatchers.is(commentDTO.getPostThatWasCommented().getTitle())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.body", CoreMatchers.is(commentDTO.getPostThatWasCommented().getBody())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.postThatWasCommented.postedAt", CoreMatchers.is(commentDTO.getPostThatWasCommented().getPostedAt())));
    }

    @Test
    void updateCommentFailed() throws Exception {

        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);
        CommentDTO commentDTO = CommentDTO.toModel(comment);

        given(commentService.updateComment(any(Long.class), eq(post.getId()), eq(comment.getId()), any(Comment.class))).willThrow(UserDoesntExistsException.class);
        this.mockMvc.perform(
                put("/users/{id_user}/posts/{id_post}/comments", 2L, post.getId())
                        .param("id_comment", Long.toString(commentDTO.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());

        given(commentService.updateComment(eq(user.getId()), any(Long.class), eq(comment.getId()), any(Comment.class))).willThrow(PostDoesntExistsException.class);
        this.mockMvc.perform(
                put("/users/{id_user}/posts/{id_post}/comments", user.getId(), 2L)
                        .param("id_comment", Long.toString(commentDTO.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());

        given(commentService.updateComment(eq(user.getId()), eq(post.getId()), any(Long.class), any(Comment.class))).willThrow(CommentDoesntExistsException.class);
        this.mockMvc.perform(
                put("/users/{id_user}/posts/{id_post}/comments", user.getId(), post.getId())
                        .param("id_comment", Long.toString(2L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());


        User user2 = new User(2L, "baibaali2", "passw2");

        given(commentService.updateComment(any(Long.class), any(Long.class), any(Long.class), any(Comment.class))).willThrow(BadPermissionException.class);
        this.mockMvc.perform(
                put("/users/{id_user}/posts/{id_post}/comments", user2.getId(), post.getId())
                .param("id_comment", Long.toString(comment.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());


        Post post2 = new Post(2L, "second post", "here we go again", user2);

        given(commentService.updateComment(any(Long.class), any(Long.class), any(Long.class), any(Comment.class))).willThrow(BadPermissionException.class);
        this.mockMvc.perform(
                put("/users/{id_user}/posts/{id_post}/comments", user2.getId(), post2.getId())
                .param("id_comment", Long.toString(2L))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"commentBody\":\"it's my comment\"}")
        ).andExpect(status().is4xxClientError());

    }
}