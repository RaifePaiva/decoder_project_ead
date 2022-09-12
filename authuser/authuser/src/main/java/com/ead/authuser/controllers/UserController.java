package com.ead.authuser.controllers;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.models.User;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<Page<User>> listAllUsers(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "userid", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) UUID courseId
    ){
        Page<User> users = null;

        if(courseId != null){
            users = userService.listPageUsers(SpecificationTemplate.userCourseId(courseId).and(spec), pageable);
        }else{
            users = userService.listPageUsers(spec, pageable);
        }

        if(!users.isEmpty()){
            for(User user: users.toList()){
                user.add(linkTo(methodOn(UserController.class).findUserById(user.getUserid())).withSelfRel());
            }
        }

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable UUID userId){
        Optional<User> user = userService.findUserById(userId);
        if(!user.isPresent()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUserById(@PathVariable UUID userId){
        Optional<User> user = userService.findUserById(userId);
        if(!user.isPresent()){
            return ResponseEntity.notFound().build();
        }

        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity updateUser(@PathVariable UUID userId, @RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        Optional<User> user = userService.findUserById(userId);
        if(!user.isPresent()){
            return ResponseEntity.notFound().build();
        }
        user.get().setFullName(userDto.getFullName());
        user.get().setPhoneNumber(userDto.getPhoneNumber());
        user.get().setCpf(userDto.getCpf());
        user.get().setLastUpdateDate(LocalDateTime.now());
        userService.saveUser(user.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity updateUserPassword(@PathVariable UUID userId, @RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<User> user = userService.findUserById(userId);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old Password.");
        }
        if(!user.get().getPassword().equalsIgnoreCase(userDto.getOldPassword())){
            return ResponseEntity.notFound().build();
        }
        user.get().setPassword(userDto.getPassword());
        user.get().setLastUpdateDate(LocalDateTime.now());
        userService.saveUser(user.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity updateUserImage(@PathVariable UUID userId, @RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
        Optional<User> user = userService.findUserById(userId);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old Password.");
        }

        user.get().setImageUrl(userDto.getImageUrl());
        user.get().setLastUpdateDate(LocalDateTime.now());
        userService.saveUser(user.get());
        return ResponseEntity.ok().build();
    }


}
