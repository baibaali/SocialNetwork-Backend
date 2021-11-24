package com.semestral.socialNetwork.controller;

import com.semestral.socialNetwork.entity.Post;
import com.semestral.socialNetwork.entity.User;
import com.semestral.socialNetwork.exception.PostDoesntExistsException;
import com.semestral.socialNetwork.exception.UserAlreadyExistsException;
import com.semestral.socialNetwork.exception.UserDoesntExistsException;
import com.semestral.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController{

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity registration(@RequestBody User user){
        try{
            userService.registration(user);
            return ResponseEntity.ok("User was successfully created");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }


    @GetMapping("")
    public ResponseEntity getOneUser(@RequestParam Long id){
        try{
            return ResponseEntity.ok(userService.getOneUser(id));
        } catch (UserDoesntExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id){
        try{
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (UserDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }

    @GetMapping("/{id_user}/liked_posts")
    public ResponseEntity getLikedPost(@PathVariable Long id_user){
        try {
            return ResponseEntity.ok(userService.getLikedPosts(id_user));
        } catch (UserDoesntExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred, while showing the users, who liked the post");
        }
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody User user){
        try{
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (UserDoesntExistsException | UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error was occurred");
        }
    }

}
