package com.ead.course.services;

import com.ead.course.models.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    void save(Lesson lesson);

    Optional<Lesson> findLessonIntoModule(UUID moduleId, UUID lessonId);

    void deleteLesson(Lesson lesson);

    List<Lesson> findAllLessonByModule(UUID moduleId);

    Page<Lesson> findAllLessonByModule(Specification<Lesson> spec, Pageable pageable);
}
