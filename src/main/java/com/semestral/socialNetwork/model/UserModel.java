package com.semestral.socialNetwork.model;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserModel {

    private Long id;
    private String username;

    private List<PostModel> posts;

    public UserModel() {
    }

    public static UserModel toModel(User userEntity){
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId());
        userModel.setUsername(userEntity.getUsername());
        userModel.setPosts(userEntity.getPosts().stream().map(PostModel::toModel).collect(Collectors.toList()));
        return userModel;
    }

    public List<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostModel> posts) {
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
