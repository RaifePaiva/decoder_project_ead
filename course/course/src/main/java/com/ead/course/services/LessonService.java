package com.ead.course.services;

import com.ead.course.models.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

    void save(Lesson lesson);

    Optional<Lesson> findLessonIntoModule(UUID moduleId, UUID lessonId);

    void deleteLesson(Lesson lesson);

    List<Lesson> findAllLessonByModule(UUID moduleId);
}
