package com.agilexp.storage;

import com.agilexp.model.*;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(TaskContent taskContent, Long id);

    void store(SolutionContent solutionContent);

    void store(ExerciseContent exerciseContent);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}