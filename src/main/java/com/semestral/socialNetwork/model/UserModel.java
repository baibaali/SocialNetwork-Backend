package com.semestral.socialNetwork.model;

import com.semestral.socialNetwork.entity.User;

public class UserModel {

    private Long id;
    private String username;

    public UserModel() {
    }

    public static UserModel toModel(User userEntity){
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId());
        userModel.setUsername(userEntity.getUsername());
        return userModel;
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
