package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.BadPermissionException;
import com.semestral.socialNetwork.exception.CommentDoesntExistsException;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.model.CommentModel;
import com.semestral.socialNetwork.repository.CommentRepository;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private CommentRepository commentRepository;

    @Test
    void writeComment() throws PostDoesntExistsException, UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);
        CommentModel commentModel = CommentModel.toModel(comment);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(postRepository.findById(1L)).willReturn(java.util.Optional.of(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentModel createdCommentModel = commentService.writeComment(user.getId(), post.getId(), comment);

        Assertions.assertEquals(commentModel.getId(), createdCommentModel.getId());
        Assertions.assertEquals(commentModel.getCommentBody(), createdCommentModel.getCommentBody());
        Assertions.assertEquals(commentModel.getCommentTime(), createdCommentModel.getCommentTime());
        Assertions.assertEquals(commentModel.getCommentOwner().getId(), createdCommentModel.getCommentOwner().getId());
        Assertions.assertEquals(commentModel.getCommentOwner().getUsername(), createdCommentModel.getCommentOwner().getUsername());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getId(), createdCommentModel.getPostThatWasCommented().getId());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getTitle(), createdCommentModel.getPostThatWasCommented().getTitle());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getBody(), createdCommentModel.getPostThatWasCommented().getBody());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getPostedAt(), createdCommentModel.getPostThatWasCommented().getPostedAt());

        Mockito.verify(commentRepository, atLeastOnce()).save(comment);

    }

    @Test
    void deleteComment() throws CommentDoesntExistsException, PostDoesntExistsException, UserDoesntExistsException, BadPermissionException {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);

        given(userRepository.findById(user.getId())).willReturn(java.util.Optional.of(user));
        given(postRepository.findById(post.getId())).willReturn(java.util.Optional.of(post));
        given(commentRepository.findById(comment.getId())).willReturn(java.util.Optional.of(comment));

        Long deletedCommentId = commentService.deleteComment(user.getId(), post.getId(), comment.getId());

        Assertions.assertEquals(comment.getId(), deletedCommentId);

        Mockito.verify(commentRepository, atLeastOnce()).deleteById(comment.getId());

    }

    @Test
    void updateComment() throws CommentDoesntExistsException, PostDoesntExistsException, UserDoesntExistsException, BadPermissionException {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment  = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);
        CommentModel commentModel = CommentModel.toModel(comment);

        given(userRepository.findById(user.getId())).willReturn(java.util.Optional.of(user));
        given(postRepository.findById(post.getId())).willReturn(java.util.Optional.of(post));
        given(commentRepository.findById(comment.getId())).willReturn(java.util.Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentModel updatedComment = commentService.updateComment(user.getId(), post.getId(), comment.getId(), comment);

        Assertions.assertEquals(commentModel.getId(), updatedComment.getId());
        Assertions.assertEquals(commentModel.getCommentBody(), updatedComment.getCommentBody());
        Assertions.assertEquals(commentModel.getCommentTime(), updatedComment.getCommentTime());
        Assertions.assertEquals(commentModel.getCommentOwner().getId(), updatedComment.getCommentOwner().getId());
        Assertions.assertEquals(commentModel.getCommentOwner().getUsername(), updatedComment.getCommentOwner().getUsername());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getId(), updatedComment.getPostThatWasCommented().getId());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getTitle(), updatedComment.getPostThatWasCommented().getTitle());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getBody(), updatedComment.getPostThatWasCommented().getBody());
        Assertions.assertEquals(commentModel.getPostThatWasCommented().getPostedAt(), updatedComment.getPostThatWasCommented().getPostedAt());

        Mockito.verify(commentRepository, atLeastOnce()).findById(comment.getId());
        Mockito.verify(commentRepository, atLeastOnce()).save(comment);

    }

}