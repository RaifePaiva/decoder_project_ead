package com.ead.course.services.impl;

import com.ead.course.models.Course;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findCourseById(UUID id) {
        return courseRepository.findById(id);
    }

    @Override
    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }
}
