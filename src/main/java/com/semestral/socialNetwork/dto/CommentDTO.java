package com.semestral.socialNetwork.dto;

import com.semestral.socialNetwork.entity.Comment;

public class CommentDTO {

    private Long id;
    private String commentBody;
    private String commentTime;
    private UserDTOWithoutPostsList commentOwner;
    private PostDTOWithoutUsersList postThatWasCommented;

    public static CommentDTO toModel(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setCommentBody(comment.getCommentBody());
        commentDTO.setCommentTime(comment.getCommentTime());
        commentDTO.setCommentOwner(UserDTOWithoutPostsList.toModel(comment.getUser()));
        commentDTO.setPostThatWasCommented(PostDTOWithoutUsersList.toModel(comment.getPost()));
        return commentDTO;
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

    public UserDTOWithoutPostsList getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(UserDTOWithoutPostsList commentOwner) {
        this.commentOwner = commentOwner;
    }

    public PostDTOWithoutUsersList getPostThatWasCommented() {
        return postThatWasCommented;
    }

    public void setPostThatWasCommented(PostDTOWithoutUsersList postThatWasCommented) {
        this.postThatWasCommented = postThatWasCommented;
    }
}
