package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.Course;
import com.ead.course.models.CourseUser;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private AuthUserClient authUserClient;


    @Override
    public boolean existsByCourseAndUserId(Course course, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(course, userId);
    }

    @Override
    public CourseUser save(CourseUser courseUser) {
        return courseUserRepository.save(courseUser);
    }

    @Transactional
    @Override
    public CourseUser saveAndSendSubscriptionUserInCourse(CourseUser courseUser) {
        courseUser = courseUserRepository.save(courseUser);
        authUserClient.postSubscriptionUserInCourse(courseUser.getCourse().getCourseId(), courseUser.getUserId());
        return courseUser;
    }
}
