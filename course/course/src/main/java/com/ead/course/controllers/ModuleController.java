package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.Course;
import com.ead.course.models.Module;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;


    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity saveModule(@RequestBody @Valid ModuleDto moduleDto, @PathVariable(value = "courseId") UUID courseId){

        Optional<Course> courseOptional = courseService.findCourseById(courseId);
        if(!courseOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var module = new Module();
        BeanUtils.copyProperties(moduleDto, module);
        module.setCourse(courseOptional.get());
        moduleService.saveModule(module);
        return ResponseEntity.status(201).build();

    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity deleteModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId){

        Optional<Module> moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        moduleService.delete(moduleOptional.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity updateModule(
            @PathVariable(value = "courseId") UUID courseId,
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody @Valid ModuleDto moduleDto
    ){

        Optional<Module> moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var module = moduleOptional.get();
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());

        moduleService.saveModule(module);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<Module>> getAllModulesByCourse(@PathVariable(value = "courseId") UUID courseId,
                                                              SpecificationTemplate.ModuleSpec spec, @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok().body(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Module> getModuleIntoCourseById(@PathVariable(value = "courseId") UUID courseId,
                                                    @PathVariable(value = "moduleId") UUID moduleId){

        Optional<Module> moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().body(moduleOptional.get());

    }





}
