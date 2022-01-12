package com.semestral.socialNetwork.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> comments;

    @ManyToMany(mappedBy = "usersWhoLiked")
    private List<Post> likedPosts;



    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.posts = new ArrayList<>();
        this.likedPosts = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public void addLikedPost(Post post) {
        if (this.likedPosts.contains(post))
            this.likedPosts.remove(post);
        else
            this.likedPosts.add(post);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComment(Comment comment) {
        this.comments.add(comment);
    }
}
