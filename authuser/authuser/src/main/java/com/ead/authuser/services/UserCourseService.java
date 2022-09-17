package com.ead.authuser.services;

import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;

import java.util.UUID;

public interface UserCourseService {


    boolean existsByUserAndCourseId(User user, UUID courseId);

    UserCourse save(UserCourse convertToUserCourse);
}
