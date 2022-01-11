package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.service.CommentService;
import com.semestral.socialNetwork.service.PostService;
import com.semestral.socialNetwork.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody Post post, @RequestParam Long user_id) throws UserDoesntExistsException {
        try{
            return ResponseEntity.ok(postService.createPost(post, user_id));
        } catch (UserDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred :(");
        }
    }

    @PostMapping("/{id_post}")
    public ResponseEntity setLike(@PathVariable Long id_post, @RequestParam Long id_user) {
        try {
            return ResponseEntity.ok(postService.setLike(id_user, id_post));
        } catch (UserDoesntExistsException | PostDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred, while liking the post");
        }
    }

    @GetMapping("/{id_post}/liked")
    public ResponseEntity getUsersWhoLikedPost(@PathVariable Long id_post){
        try {
            return ResponseEntity.ok(postService.getUsersWhoLiked(id_post));
        } catch (PostDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred, while showing the users, who liked the post");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Long id){
        try{
            return ResponseEntity.ok(postService.deletePost(id));
        } catch (PostDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity updatePost(@PathVariable Long id, @RequestBody Post post){
        try{
            return ResponseEntity.ok(postService.updatePost(id, post));
        } catch (PostDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }


}
