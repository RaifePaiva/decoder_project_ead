package com.ead.authuser.controllers;

import com.ead.authuser.dto.InstructorDto;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.User;
import com.ead.authuser.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    private UserService userService;

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto){
        Optional<User> userOptional = userService.findUserById(instructorDto.getUserId());
        if(!userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }else {
            var user = userOptional.get();
            user.setUserType(UserType.INSTRUCTOR);
            user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }


}
