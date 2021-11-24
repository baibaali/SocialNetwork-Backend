package com.semestral.socialNetwork.model;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

public class CommentModel {

    private Long id;
    private String commentBody;
    private String commentTime;
    private UserModelWithoutPostsList commentOwner;
    private PostModelWithoutUsersList postThatWasCommented;

    public static CommentModel toModel(Comment comment) {
        CommentModel commentModel = new CommentModel();
        commentModel.setId(comment.getId());
        commentModel.setCommentBody(comment.getCommentBody());
        commentModel.setCommentTime(comment.getCommentTime());
        commentModel.setCommentOwner(UserModelWithoutPostsList.toModel(comment.getUser()));
        commentModel.setPostThatWasCommented(PostModelWithoutUsersList.toModel(comment.getPost()));
        return commentModel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public UserModelWithoutPostsList getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(UserModelWithoutPostsList commentOwner) {
        this.commentOwner = commentOwner;
    }

    public PostModelWithoutUsersList getPostThatWasCommented() {
        return postThatWasCommented;
    }

    public void setPostThatWasCommented(PostModelWithoutUsersList postThatWasCommented) {
        this.postThatWasCommented = postThatWasCommented;
    }
}
