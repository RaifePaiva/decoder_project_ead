package com.ead.course.services.impl;

import com.ead.course.models.Course;
import com.ead.course.models.CourseUser;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseUserRepository courseUserRepository;

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
        List<Module> moduleList = moduleRepository.findAllModulesIntoCourse(course.getCourseId());
        if(!moduleList.isEmpty()){
            for(Module module : moduleList){
                List<Lesson> lessonList = lessonRepository.findAllLessonByModule(module.getModuleId());
                if(!lessonList.isEmpty()){
                    lessonRepository.deleteAll();
                }
            }
            moduleRepository.deleteAll(moduleList);
        }
        List<CourseUser> courseUserList = courseUserRepository.findAllCourseUserIntoCourse(course.getCourseId());
        if(!courseUserList.isEmpty()){
            courseUserRepository.deleteAll(courseUserList);
        }

        courseRepository.delete(course);
    }

    @Override
    public Page<Course> findAllCourses(Specification<Course> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
