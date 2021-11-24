package com.semestral.socialNetwork.model;

import com.semestral.socialNetwork.entity.Post;
import java.util.List;
import java.util.stream.Collectors;

public class PostModelWithoutUsersList {

    private Long id;
    private String title;
    private String body;
    private String postedAt;

    public static PostModelWithoutUsersList toModel(Post post) {
        PostModelWithoutUsersList postModel = new PostModelWithoutUsersList();
        postModel.setId(post.getId());
        postModel.setTitle(post.getTitle());
        postModel.setBody(post.getBody());
        postModel.setPostedAt(post.getPostedAt());
        return postModel;
    }

    public PostModelWithoutUsersList() {
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
