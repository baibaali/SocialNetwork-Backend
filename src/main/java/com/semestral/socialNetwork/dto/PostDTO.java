package com.semestral.socialNetwork.dto;

import com.semestral.socialNetwork.entity.Post;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {

    private Long id;
    private String title;
    private String body;
    private String postedAt;
    private List<UserDTOWithoutPostsList> usersWhoLiked;

    public static PostDTO toModel(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setBody(post.getBody());
        postDTO.setPostedAt(post.getPostedAt());
        postDTO.setUsersWhoLiked(post.getUsersWhoLiked().stream().map(UserDTOWithoutPostsList::toModel).collect(Collectors.toList()));
        return postDTO;
    }

    public PostDTO() {
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

    public List<UserDTOWithoutPostsList> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(List<UserDTOWithoutPostsList> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }
}
