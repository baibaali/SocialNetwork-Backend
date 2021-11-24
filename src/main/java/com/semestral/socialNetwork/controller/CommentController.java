package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.service.CommentService;
import com.semestral.socialNetwork.service.PostService;
import com.semestral.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class CommentController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/{id_user}/posts/{id_post}")
    public ResponseEntity writeComment(@PathVariable Long id_user, @PathVariable Long id_post, @RequestBody Comment comment){
        try{
            return ResponseEntity.ok(commentService.writeComment(id_user, id_post, comment));
        } catch (PostDoesntExistsException | UserDoesntExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occured");
        }

    }
}
