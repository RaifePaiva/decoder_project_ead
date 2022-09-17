package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.Course;
import com.ead.course.models.CourseUser;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private AuthUserClient authUserClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUsersByUser(
            @PageableDefault(page = 0, size = 10, sort = "userid", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable UUID courseId
    ){
        return ResponseEntity.ok().body(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDto subscriptionDto
    ){

        ResponseEntity<UserDto> responseUser;

        Optional<Course> courseOptional = courseService.findCourseById(courseId);
        if(!courseOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        if(courseUserService.existsByCourseAndUserId(courseOptional.get(), subscriptionDto.getUserId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        try {
            responseUser = authUserClient.getOneUserById(subscriptionDto.getUserId());
            if(responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
            }
        }catch (HttpStatusCodeException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        }

        CourseUser courseUser = courseUserService.saveAndSendSubscriptionUserInCourse(courseOptional.get().convertToCourseUserModel(subscriptionDto.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUser);
    }

}
