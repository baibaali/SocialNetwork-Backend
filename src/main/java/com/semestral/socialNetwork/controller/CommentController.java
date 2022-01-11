package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.entity.Comment;
import com.semestral.socialNetwork.entity.Post;
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

    @DeleteMapping("/{id_user}/posts/{id_post}/comments/{id_comment}")
    public ResponseEntity deleteComment(@PathVariable Long id_user, @PathVariable Long id_post, @PathVariable Long id_comment){
        try{
            return ResponseEntity.ok(commentService.deleteComment(id_user, id_post, id_comment));
        } catch (PostDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }

    @PutMapping("/{id_user}/posts/{id_post}/comments")
    public ResponseEntity updateComment(@PathVariable Long id_user, @PathVariable Long id_post,
                                     @RequestParam Long id_comment, @RequestBody Comment comment){
        try{
            return ResponseEntity.ok(commentService.updateComment(id_user, id_post, id_comment, comment));
        } catch (PostDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }
}
