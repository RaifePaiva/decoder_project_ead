package com.ead.authuser.dto;

import com.ead.authuser.enums.CourseStatus;
import com.ead.authuser.enums.Courselevel;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseDto {

    private UUID courseId;

    private String name;

    private String description;

    private String imageUrl;

    private CourseStatus courseStatus;

    private Courselevel courseLevel;

    private UUID userInstructor;

}
