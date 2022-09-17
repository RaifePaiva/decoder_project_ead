package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dto.CourseDto;
import com.ead.authuser.dto.UserCourseDto;
import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @Autowired
    private CourseClient userClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;

    @GetMapping("users/{userId}/courses")
    public ResponseEntity<Page<CourseDto>> getAllCoursesByUser(
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable UUID userId
    ){
        return ResponseEntity.ok().body(userClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable UUID userId,
            @RequestBody @Valid UserCourseDto userCourseDto
    ){
        Optional<User> userOptional = userService.findUserById(userId);
        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if(userCourseService.existsByUserAndCourseId(userOptional.get(), userCourseDto.getCourseId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        UserCourse userCourse = userCourseService.save(userOptional.get().convertToUserCourse(userCourseDto.getCourseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourse);
    }

}
