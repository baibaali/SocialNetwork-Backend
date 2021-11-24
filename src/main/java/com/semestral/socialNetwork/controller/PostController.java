package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.service.PostService;
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
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }

}
