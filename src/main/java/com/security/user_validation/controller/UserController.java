package com.security.user_validation.controller;

import com.security.user_validation.model.UserLoginDTO;
import com.security.user_validation.model.Users;
import com.security.user_validation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAllUsers() {
        return "Hello";
    }

    @GetMapping("/{username}")
    public ResponseEntity<Users> findUser(@PathVariable String username){
        Users user = userService.findUser(username);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user){
        if(userService.findUser(user.getUsername()) != null){
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }
        Users userDetails = userService.saveUser(user);
        if(userDetails == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userDetails);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> delete(@PathVariable String username){
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> update(@RequestBody Users user,
                                        @PathVariable String username
    ){
        if(userService.findUser(user.getUsername()) != null){
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }
        Users userDetails = userService.updateUser(user, username);
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/login")
    public ResponseEntity<Users> login(@RequestBody UserLoginDTO userDTO){
        boolean authenticated = userService.verify(userDTO);
        System.out.println(authenticated);
        return authenticated ?
                ResponseEntity.ok(userService.findUser(userDTO.getUsername()))
                : ResponseEntity.badRequest().build();
    }
}
