package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.Course;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@Log4j2
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseValidator courseValidator;


    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto, Errors errors){
        log.debug("POST saveCourse courseDto received {} ", courseDto.toString());
        courseValidator.validate(courseDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        var course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        courseService.save(course);
        log.debug("POST saveCourse course saved {} ", course.toString());
        log.info("Course saved successfully courseId {}", course.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<Course> courseOptional = courseService.findCourseById(courseId);
        if(!courseOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        courseService.deleteCourse(courseOptional.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDto courseDto){
        Optional<Course> courseOptional = courseService.findCourseById(courseId);
        if(!courseOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var course = courseOptional.get();
        BeanUtils.copyProperties(courseDto, course);
        course.setLastUpdateDate(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(course));
    }

    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourses(
            SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) UUID userId
    ){
        if(userId != null){
            return ResponseEntity.status(HttpStatus.OK).body(courseService.findAllCourses(SpecificationTemplate.courseUserId(userId).and(spec), pageable));
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAllCourses(spec, pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable(value = "courseId") UUID courseId){
        Optional<Course> courseOptional = courseService.findCourseById(courseId);
        if(!courseOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(courseOptional.get());
    }


}
