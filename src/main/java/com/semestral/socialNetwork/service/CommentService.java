package com.semestral.socialNetwork.service;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.exception.BadPermissionException;
import com.semestral.socialNetwork.exception.CommentDoesntExistsException;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.dto.CommentDTO;
import com.semestral.socialNetwork.repository.CommentRepository;
import com.semestral.socialNetwork.repository.PostRepository;
import com.semestral.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;


    public CommentDTO writeComment(Long id_user, Long id_post, Comment comment) throws UserDoesntExistsException, PostDoesntExistsException {
        if (userRepository.findById(id_user).isEmpty())
            throw new UserDoesntExistsException("User with specified id is not found");
        if (postRepository.findById(id_post).isEmpty())
            throw new PostDoesntExistsException("Post with specified id is not found");
        comment.setPost(postRepository.findById(id_post).get());
        comment.setUser(userRepository.findById(id_user).get());
        comment.setCommentTime(Post.getCurrentTimeStamp());
        postRepository.findById(id_post).get().setComment(comment);
        userRepository.findById(id_user).get().setComment(comment);
        return CommentDTO.toModel(commentRepository.save(comment));
    }

    public Long deleteComment(Long id_user, Long id_post, Long id_comment) throws PostDoesntExistsException, UserDoesntExistsException, CommentDoesntExistsException, BadPermissionException {
        validateComment(id_user, id_post, id_comment);
        commentRepository.deleteById(id_comment);
        return id_comment;
    }

    public CommentDTO updateComment(Long id_user, Long id_post, Long id_comment, Comment updatedComment) throws UserDoesntExistsException, PostDoesntExistsException, CommentDoesntExistsException, BadPermissionException {
        validateComment(id_user, id_post, id_comment);
        Comment comment = commentRepository.findById(id_comment).get();

        boolean isChanged = false;

        if (updatedComment.getCommentBody() != null && !Objects.equals(comment.getCommentBody(), updatedComment.getCommentBody())) {
            comment.setCommentBody(updatedComment.getCommentBody());
            isChanged = true;
        }
        if (isChanged)
            comment.setCommentTime(Post.getCurrentTimeStamp());

        return CommentDTO.toModel(commentRepository.save(comment));

    }

    public void validateComment(Long id_user, Long id_post, Long id_comment) throws UserDoesntExistsException, PostDoesntExistsException, CommentDoesntExistsException, BadPermissionException {
        if (userRepository.findById(id_user).isEmpty())
            throw new UserDoesntExistsException("User with specified id is not found");
        if (postRepository.findById(id_post).isEmpty())
            throw new PostDoesntExistsException("Post with specified id is not found");
        if (commentRepository.findById(id_comment).isEmpty())
            throw new CommentDoesntExistsException("Comment with specified id is not found");
        if (commentRepository.findById(id_comment).get().getUser().getId() != id_user)
            throw new BadPermissionException("You cannot delete the comment, because you not the owner");
        if (commentRepository.findById(id_comment).get().getPost().getId() != id_post) {
            throw new BadPermissionException("You cannot delete the comment, because it is not the part of this post");
        }
    }
}
