package com.semestral.socialNetwork.model;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserModelWithoutPostsList {

    private Long id;
    private String username;

    public UserModelWithoutPostsList() {
    }

    public static UserModelWithoutPostsList toModel(User userEntity){
        UserModelWithoutPostsList userModel = new UserModelWithoutPostsList();
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