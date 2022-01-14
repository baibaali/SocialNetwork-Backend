package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.BadPermissionException;
import com.semestral.socialNetwork.exception.CommentDoesntExistsException;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.dto.CommentDTO;
import com.semestral.socialNetwork.repository.CommentRepository;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;

//@WebMvcTest(CommentService.class)
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void writeComment() throws PostDoesntExistsException, UserDoesntExistsException {
        User user = new User(1L, "baibaali", "passw");
        Post post = new Post(1L, "some title", "some body", user);
        Comment comment = new Comment(1L, "it's my comment", Post.getCurrentTimeStamp(), post, user);
        CommentDTO commentDTO = CommentDTO.toModel(comment);

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(postRepository.findById(1L)).willReturn(java.util.Optional.of(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentDTO createdCommentDTO = commentService.writeComment(user.getId(), post.getId(), comment);

        Assertions.assertEquals(commentDTO.getId(), createdCommentDTO.getId());
        Assertions.assertEquals(commentDTO.getCommentBody(), createdCommentDTO.getCommentBody());
        Assertions.assertEquals(commentDTO.getCommentTime(), createdCommentDTO.getCommentTime());
        Assertions.assertEquals(commentDTO.getCommentOwner().getId(), createdCommentDTO.getCommentOwner().getId());
        Assertions.assertEquals(commentDTO.getCommentOwner().getUsername(), createdCommentDTO.getCommentOwner().getUsername());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getId(), createdCommentDTO.getPostThatWasCommented().getId());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getTitle(), createdCommentDTO.getPostThatWasCommented().getTitle());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getBody(), createdCommentDTO.getPostThatWasCommented().getBody());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getPostedAt(), createdCommentDTO.getPostThatWasCommented().getPostedAt());

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
        CommentDTO commentDTO = CommentDTO.toModel(comment);

        given(userRepository.findById(user.getId())).willReturn(java.util.Optional.of(user));
        given(postRepository.findById(post.getId())).willReturn(java.util.Optional.of(post));
        given(commentRepository.findById(comment.getId())).willReturn(java.util.Optional.of(comment));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        CommentDTO updatedComment = commentService.updateComment(user.getId(), post.getId(), comment.getId(), comment);

        Assertions.assertEquals(commentDTO.getId(), updatedComment.getId());
        Assertions.assertEquals(commentDTO.getCommentBody(), updatedComment.getCommentBody());
        Assertions.assertEquals(commentDTO.getCommentTime(), updatedComment.getCommentTime());
        Assertions.assertEquals(commentDTO.getCommentOwner().getId(), updatedComment.getCommentOwner().getId());
        Assertions.assertEquals(commentDTO.getCommentOwner().getUsername(), updatedComment.getCommentOwner().getUsername());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getId(), updatedComment.getPostThatWasCommented().getId());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getTitle(), updatedComment.getPostThatWasCommented().getTitle());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getBody(), updatedComment.getPostThatWasCommented().getBody());
        Assertions.assertEquals(commentDTO.getPostThatWasCommented().getPostedAt(), updatedComment.getPostThatWasCommented().getPostedAt());

        Mockito.verify(commentRepository, atLeastOnce()).findById(comment.getId());
        Mockito.verify(commentRepository, atLeastOnce()).save(comment);

    }

}