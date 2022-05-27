package com.ead.course.services;

import com.ead.course.models.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {
    Course save(Course course);

    Optional<Course> findCourseById(UUID id);

    void deleteCourse(Course course);

    List<Course> findAllCourses();
}
