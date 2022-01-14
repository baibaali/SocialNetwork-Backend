package com.semestral.socialNetwork.dto;

import com.semestral.socialNetwork.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    private String username;

    private List<PostDTOWithoutUsersList> posts;
    private List<PostDTOWithoutUsersList> likedPosts;

    public UserDTO(){
    }

    public UserDTO(long id, String username) {
        this.username = username;
        this.id = id;
        posts = new ArrayList<>();
        likedPosts = new ArrayList<>();
    }

    public static UserDTO toModel(User userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setPosts(userEntity.getPosts().stream().map(PostDTOWithoutUsersList::toModel).collect(Collectors.toList()));
        userDTO.setLikedPosts(userEntity.getLikedPosts().stream().map(PostDTOWithoutUsersList::toModel).collect(Collectors.toList()));
        return userDTO;
    }

    public List<PostDTOWithoutUsersList> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTOWithoutUsersList> posts) {
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

    public List<PostDTOWithoutUsersList> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<PostDTOWithoutUsersList> likedPosts) {
        this.likedPosts = likedPosts;
    }
}
