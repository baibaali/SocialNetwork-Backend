package com.semestral.socialNetwork.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(name="post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private String postedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "user_liked", joinColumns = @JoinColumn(name = "post_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersWhoLiked;

    public Post() {
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

    public void setPostedAt() {
        this.postedAt = getCurrentTimeStamp();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public List<User> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(List<User> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }

    public void addLike(User user){
        if (this.usersWhoLiked.contains(user))
            this.usersWhoLiked.remove(user);
        else
            this.usersWhoLiked.add(user);
    }
}
