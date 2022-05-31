package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.Course;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleService moduleService;


    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity saveLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody @Valid LessonDto lessonDto
    ){

        Optional<Module> moduleOptional = moduleService.findModuleById(moduleId);
        if(!moduleOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var lesson = new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);
        lesson.setModule(moduleOptional.get());
        lessonService.save(lesson);
        return ResponseEntity.status(201).build();

    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity deleteLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId
    ){

        Optional<Lesson> lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(!lessonOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        lessonService.deleteLesson(lessonOptional.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity updateLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId,
            @RequestBody @Valid LessonDto lessonDto
    ){

        Optional<Lesson> lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(!lessonOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var lesson = lessonOptional.get();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setDescription(lessonDto.getDescription());
        lesson.setVideoUrl(lessonDto.getVideoUrl());
        lessonService.save(lesson);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<Lesson>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId){
        return ResponseEntity.ok().body(lessonService.findAllLessonByModule(moduleId));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Lesson> getModuleIntoCourseById(
            @PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId
    ){

        Optional<Lesson> lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(!lessonOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().body(lessonOptional.get());

    }


}
