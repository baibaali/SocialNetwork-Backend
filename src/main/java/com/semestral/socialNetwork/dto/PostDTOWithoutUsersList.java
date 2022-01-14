package com.semestral.socialNetwork.dto;

import com.semestral.socialNetwork.entity.Post;

public class PostDTOWithoutUsersList {

    private Long id;
    private String title;
    private String body;
    private String postedAt;

    public static PostDTOWithoutUsersList toModel(Post post) {
        PostDTOWithoutUsersList postModel = new PostDTOWithoutUsersList();
        postModel.setId(post.getId());
        postModel.setTitle(post.getTitle());
        postModel.setBody(post.getBody());
        postModel.setPostedAt(post.getPostedAt());
        return postModel;
    }

    public PostDTOWithoutUsersList() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

}
