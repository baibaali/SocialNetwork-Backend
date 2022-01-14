package com.semestral.socialNetwork.dto;

import com.semestral.socialNetwork.entity.User;

public class UserDTOWithoutPostsList {

    private Long id;
    private String username;

    public UserDTOWithoutPostsList() {
    }

    public static UserDTOWithoutPostsList toModel(User userEntity){
        UserDTOWithoutPostsList userModel = new UserDTOWithoutPostsList();
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
