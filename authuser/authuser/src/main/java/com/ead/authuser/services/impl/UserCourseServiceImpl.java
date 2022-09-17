package com.ead.authuser.services.impl;

import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserCourseRepository userCourseRepository;


    @Override
    public boolean existsByUserAndCourseId(User user, UUID courseId) {
        return userCourseRepository.existsByUserAndCourseId(user, courseId);
    }

    @Override
    public UserCourse save(UserCourse userCourse) {
        return userCourseRepository.save(userCourse);
    }
}
