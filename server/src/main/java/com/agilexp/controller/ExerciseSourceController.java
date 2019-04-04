package com.agilexp.controller;

import com.agilexp.model.ExerciseSource;
import com.agilexp.repository.ExerciseSourceRepository;
import com.agilexp.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ExerciseSourceController {
    @Autowired
    ExerciseSourceRepository repository;
    private final StorageService storageService;

    @Autowired
    public ExerciseSourceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/exercise-sources/create")
    public ExerciseSource postExerciseSource(@RequestBody ExerciseSource sourceCode) {

        ExerciseSource _sourceCode = repository.save(new ExerciseSource(
                sourceCode.getExerciseId(),
                sourceCode.getFileName(),
                sourceCode.getContent()

        ));

        System.out.format("Created source code %s for exercise #%s\n", sourceCode.getFileName(), sourceCode.getExerciseId());
        return _sourceCode;
    }

    @GetMapping(value="/exercise-sources/exercise/{exerciseId}")
    public List<ExerciseSource> getExerciseSourcesByExerciseId(@PathVariable("exerciseId") long exerciseId) {
        System.out.println("Get exercise sources with exercise id " + exerciseId + "...");

        List<ExerciseSource> exerciseSources = new ArrayList<>(repository.findExerciseSourcesByExerciseId(exerciseId));
        return exerciseSources;
    }
}
