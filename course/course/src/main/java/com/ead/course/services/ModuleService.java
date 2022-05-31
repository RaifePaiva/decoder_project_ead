package com.ead.course.services;

import com.ead.course.models.Module;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {

    void saveModule(Module module);

    Optional<Module> findModuleIntoCourse(UUID courseId, UUID moduleId);

    void delete(Module module);

    List<Module> findAllByCourse(UUID courseId);

    Optional<Module> findModuleById(UUID moduleId);
}
