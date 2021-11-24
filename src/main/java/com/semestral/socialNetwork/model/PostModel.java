package com.semestral.socialNetwork.model;

import com.semestral.socialNetwork.entity.Post;
import java.util.List;
import java.util.stream.Collectors;

public class PostModel {

    private Long id;
    private String title;
    private String body;
    private String postedAt;
    private List<UserModelWithoutPostsList> usersWhoLiked;

    public static PostModel toModel(Post post) {
        PostModel postModel = new PostModel();
        postModel.setId(post.getId());
        postModel.setTitle(post.getTitle());
        postModel.setBody(post.getBody());
        postModel.setPostedAt(post.getPostedAt());
        postModel.setUsersWhoLiked(post.getUsersWhoLiked().stream().map(UserModelWithoutPostsList::toModel).collect(Collectors.toList()));
        return postModel;
    }

    public PostModel() {
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

    public List<UserModelWithoutPostsList> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(List<UserModelWithoutPostsList> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }
}