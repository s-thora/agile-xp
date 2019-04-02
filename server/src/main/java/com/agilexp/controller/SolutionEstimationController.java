package com.agilexp.controller;

import com.agilexp.compiler.Compiler;
import com.agilexp.compiler.exception.CompilationFailedException;
import com.agilexp.model.*;
import com.agilexp.repository.*;
import com.agilexp.storage.StorageException;
import com.agilexp.storage.StorageService;
import com.agilexp.tester.Tester;
import com.agilexp.tester.exception.TestFailedException;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class SolutionEstimationController {
    @Autowired
    private SolutionEstimationRepository repository;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private SolutionSourceRepository solutionSourceRepository;

    @Autowired
    private SolutionTestRepository solutionTestRepository;

    @Autowired
    private SolutionFileRepository solutionFileRepository;

    @Autowired
    private ExerciseSourceRepository exerciseSourceRepository;

    @Autowired
    private ExerciseTestRepository exerciseTestRepository;

    @Autowired
    private ExerciseFileRepository exerciseFileRepository;

    @Autowired
    private ExerciseSwitcherRepository exerciseSwitcherRepository;

    @Autowired
    private ExerciseFlagsRepository exerciseFlagsRepository;

    private final StorageService storageService;

    private final String PUBLIC = "Public";
    private final String PRIVATE = "Private";

    @Autowired
    public SolutionEstimationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/solution-estimations/estimate/source-test/{solutionId}")
    public SolutionEstimation getSolutionSourceTestEstimation(@PathVariable long solutionId) {
        List<List<? extends SolutionContent>> solutionContents = List.of(
                solutionSourceRepository.findBySolutionId(solutionId),
                solutionTestRepository.findBySolutionId(solutionId));

        Solution solution = solutionRepository.findById(solutionId);
        List<List<? extends ExerciseContent>> exerciseContents = List.of(
                exerciseTestRepository.findExerciseTestsByExerciseId(solution.getExerciseId()));

        SolutionEstimation solutionEstimation = new SolutionEstimation(solutionId);

        String publicEstimation = estimatePublic(solutionContents, solutionId);
        String privateEstimation = estimatePrivate(solutionContents, exerciseContents, solutionId);
        solutionEstimation.setEstimation(privateEstimation + publicEstimation);

        removeTempFiles();

        SolutionEstimation _solutionEstimation = repository.save(solutionEstimation);
        System.out.format("Created solution estimation %s\n", _solutionEstimation);
        return _solutionEstimation;
    }

    @GetMapping(value = "/solution-estimations/estimate/source-test-file/{solutionId}")
    public SolutionEstimation getSolutionSourceTestFileEstimation(@PathVariable long solutionId) {
        List<List<? extends SolutionContent>> solutionContents = List.of(
                solutionSourceRepository.findBySolutionId(solutionId),
                solutionTestRepository.findBySolutionId(solutionId),
                solutionFileRepository.findBySolutionId(solutionId));

        Solution solution = solutionRepository.findById(solutionId);
        List<List<? extends ExerciseContent>> exerciseContents = List.of(
                exerciseTestRepository.findExerciseTestsByExerciseId(solution.getExerciseId())
//                 exerciseFileRepository.findExerciseFilesByExerciseId(solution.getExerciseId())
//                 TODO: 02-Apr-19 exerciseFiles when solutionFiles work completely
        );

        SolutionEstimation solutionEstimation = new SolutionEstimation(solutionId);

        String publicEstimation = estimatePublic(solutionContents, solutionId);
        String privateEstimation = estimatePrivate(solutionContents, exerciseContents, solutionId);
        solutionEstimation.setEstimation(privateEstimation + publicEstimation);

        removeTempFiles();

        SolutionEstimation _solutionEstimation = repository.save(solutionEstimation);
        System.out.format("Created solution estimation %s\n", _solutionEstimation);
        return _solutionEstimation;
    }


    private String estimatePublic(List<List<? extends SolutionContent>> solutionContents, long solutionId) {
        Path outDirPath = storageService.load("solution_public" + solutionId);
        try {
            storeFiles(solutionContents, List.of());
            List<Path> paths = getPublicPaths(solutionContents);
            compileFiles(paths, outDirPath);
            List<Result> testResults = testPublicFiles(solutionContents, outDirPath);
            return getResult(testResults, PUBLIC);
        } catch (StorageException e) {
            e.printStackTrace();
            return "File storing failed: " + e.getMessage();
        } catch (CompilationFailedException e) {
            e.printStackTrace();
            return "Compilation failed: " + e.getMessage();
        } catch (TestFailedException e) {
            e.printStackTrace();
            return "Tests run failed: " + e.getMessage();
        }
    }

    private String estimatePrivate(List<List<? extends SolutionContent>> solutionContents, List<List<? extends ExerciseContent>> exerciseContents, long solutionId) {
        Path outDirPath = storageService.load("solution_private" + solutionId);
        try {
            storeFiles(solutionContents, exerciseContents);
            List<Path> paths = getPrivatePaths(solutionContents, exerciseContents);
            compileFiles(paths, outDirPath);
            List<Result> testResults = testPrivateFiles(exerciseContents, outDirPath);
            return getResult(testResults, PRIVATE);
        } catch (StorageException e) {
            e.printStackTrace();
            return "File storing failed: " + e.getMessage();
        } catch (CompilationFailedException e) {
            e.printStackTrace();
            return "Compilation failed: " + e.getMessage();
        } catch (TestFailedException e) {
            e.printStackTrace();
            return "Tests run failed: " + e.getMessage();
        }
    }


    @GetMapping(value = "/solution-estimations/estimate/test/{solutionId}")
    public SolutionEstimation getSolutionTestEstimation(@PathVariable long solutionId) {
        SolutionEstimation solutionEstimation = new SolutionEstimation(solutionId);

        String privateEstimation = estimateBlackBox(solutionId);
        solutionEstimation.setEstimation(privateEstimation);

        removeTempFiles();

        return solutionEstimation;
    }


    private String estimateBlackBox(long solutionId) {
        List<SolutionTest> solutionTests = solutionTestRepository.findBySolutionId(solutionId);
        Solution solution = solutionRepository.findById(solutionId);
        List<ExerciseTest> exerciseTests = exerciseTestRepository.findExerciseTestsByExerciseId(solution.getExerciseId());
        List<ExerciseSource> exerciseSources = exerciseSourceRepository.findExerciseSourcesByExerciseId(solution.getExerciseId());
        List<ExerciseSwitcher> exerciseSwitchers = exerciseSwitcherRepository.findExerciseSwitcherByExerciseId(solution.getExerciseId());
        List<ExerciseFlags> exerciseFlags = exerciseFlagsRepository.findExerciseFlagsByExerciseId(solution.getExerciseId());
        Path publicOutDirPath = storageService.load("solution_public_blackbox" + solutionId);
        Path privateOutDirPath = storageService.load("solution_private_blackbox" + solutionId);

        try {
            storeFiles(List.of(solutionTests), List.of(exerciseSources, exerciseTests, exerciseSwitchers, exerciseFlags));
            List<Path> publicPaths = getPublicBlackBoxPaths(solutionTests, exerciseSwitchers, exerciseSources);
            List<Path> privatePaths = getPrivateBlackBoxPaths(exerciseTests, exerciseSwitchers, exerciseSources);
            compileFiles(publicPaths, publicOutDirPath);
            compileFiles(privatePaths, privateOutDirPath);
            List<Result> publicTestResults = testPublicFiles(List.of(solutionTests), publicOutDirPath);
            List<Result> privateTestResults = testPrivateFiles(List.of(exerciseTests), privateOutDirPath);
            return getResult(publicTestResults, PUBLIC) + getResult(privateTestResults, PRIVATE);
        } catch (StorageException e) {
            e.printStackTrace();
            return "File storing failed: " + e.getMessage();
        } catch (CompilationFailedException e) {
            e.printStackTrace();
            return "Compilation failed: " + e.getMessage();
        } catch (TestFailedException e) {
            e.printStackTrace();
            return "Tests run failed: " + e.getMessage();
        }
    }

    private void storeFiles(List<List<? extends SolutionContent>> solutionContent, List<List<? extends ExerciseContent>> exerciseContent) {
        try {
            solutionContent.forEach(e -> e.forEach(storageService::store));
            exerciseContent.forEach(e -> e.forEach(storageService::store));
        } catch (StorageException e) {
            throw new StorageException(e.getMessage());
        }
    }

    private List<Path> getPublicPaths(List<List<? extends SolutionContent>> solutionContent) {
        return solutionContent.stream()
                .flatMap(Collection::stream)
                .filter(e -> e instanceof SolutionSource ||
                        e instanceof SolutionTest)
                .map(e -> storageService
                        .load("solution_content" + e.getId())
                        .resolve(e.getFileName()))
                .collect(Collectors.toList());
    }

    private List<Path> getPrivatePaths(List<List<? extends SolutionContent>> solutionContent, List<List<? extends ExerciseContent>> exerciseContent) {
        List<Path> paths = solutionContent.stream()
                .flatMap(Collection::stream)
                .filter(e -> e instanceof SolutionSource)
                .map(e -> storageService
                        .load("solution_content" + e.getId())
                        .resolve(e.getFileName()))
                .collect(Collectors.toList());
        List<Path> exercisePaths = exerciseContent.stream()
                .flatMap(Collection::stream)
                .filter(e -> e instanceof ExerciseTest)
                .map(e -> storageService
                        .load("exercise_content" + e.getId())
                        .resolve(e.getFileName()))
                .collect(Collectors.toList());
        paths.addAll(exercisePaths);
        return paths;
    }

    private List<Path> getPublicBlackBoxPaths(List<SolutionTest> solutionTests, List<ExerciseSwitcher> exerciseSwitchers, List<ExerciseSource> exerciseSources) {
        List<Path> paths = new ArrayList<>();
        solutionTests.forEach(solutionTest -> {
            paths.add(storageService
                    .load("solution_content" + solutionTest.getId())
                    .resolve(solutionTest.getFileName()));
        });
        exerciseSwitchers.forEach(exerciseSwitcher -> {
            paths.add(storageService
                    .load("exercise_content" + exerciseSwitcher.getId())
                    .resolve(exerciseSwitcher.getFileName()));
        });
        exerciseSources.forEach(exerciseSource -> {
            paths.add(storageService
                    .load("exercise_content" + exerciseSource.getId())
                    .resolve(exerciseSource.getFileName()));
        });

        return paths;
    }

    private List<Path> getPrivateBlackBoxPaths(List<ExerciseTest> exerciseTests, List<ExerciseSwitcher> exerciseSwitchers, List<ExerciseSource> exerciseSources) {
        List<Path> paths = new ArrayList<>();
        exerciseTests.forEach(exerciseTest -> {
            paths.add(storageService
                    .load("exercise_content" + exerciseTest.getId())
                    .resolve(exerciseTest.getFileName()));
        });
        exerciseSwitchers.forEach(exerciseSwitcher -> {
            paths.add(storageService
                    .load("exercise_content" + exerciseSwitcher.getId())
                    .resolve(exerciseSwitcher.getFileName()));
        });
        exerciseSources.forEach(exerciseSource -> {
            paths.add(storageService
                    .load("exercise_content" + exerciseSource.getId())
                    .resolve(exerciseSource.getFileName()));
        });

        return paths;
    }

    private void compileFiles(List<Path> filesPaths, Path outDirPath) throws CompilationFailedException {
        try {
            Compiler.compile(filesPaths, outDirPath);
        } catch (CompilationFailedException e) {
            throw new CompilationFailedException(e.getMessage());
        }
        System.out.println("compiled");
    }

    private List<Result> testPublicFiles(List<List<? extends SolutionContent>> solutionContents, Path outDirPath) throws TestFailedException {
        List<Result> solutionTestsResults = new ArrayList<>();
        for (List<? extends SolutionContent> solutionContentList : solutionContents) {
            for (SolutionContent solutionContent : solutionContentList){
                if (!solutionContent.getClass().equals(SolutionTest.class)) continue;
                try {
                    solutionTestsResults.add(Tester.test(outDirPath, solutionContent.getFileName()));
                } catch (TestFailedException e) {
                    throw new TestFailedException(e.getMessage());
                }
            }
        }
        System.out.println("tested");
        return solutionTestsResults;
    }

    private List<Result> testPrivateFiles(List<List<? extends ExerciseContent>> exerciseContents, Path outDirPath) throws TestFailedException {
        List<Result> solutionTestsResults = new ArrayList<>();
        for (List<? extends ExerciseContent> exerciseContentList : exerciseContents) {
            for (ExerciseContent exerciseContent : exerciseContentList){
                if (!exerciseContent.getClass().equals(ExerciseTest.class)) continue;
                try {
                    solutionTestsResults.add(Tester.test(outDirPath, exerciseContent.getFileName()));
                } catch (TestFailedException e) {
                    throw new TestFailedException(e.getMessage());
                }
            }
        }
        System.out.println("tested");
        return solutionTestsResults;
    }

    private void removeTempFiles() {
        storageService.deleteAll(); // clean upload-dir
        storageService.init();
    }

    private String getResult(List<Result> exerciseTestsResults, String type) {
        StringBuilder result = new StringBuilder();

        result.append(type)
            .append(" tests result:\n");
        exerciseTestsResults.forEach(exerciseTestsResult ->
            result.append(getResultInfo(exerciseTestsResult))
        );

        return result.toString();
    }

    private StringBuffer getResultInfo(Result result) {
        StringBuffer output = new StringBuffer();
        output.append("Test runtime: ").append(result.getRunTime()).append(" ms")
            .append("\nTest success: ").append(result.wasSuccessful())
            .append("\nFailures count: ").append(result.getFailureCount());
        if (result.getFailureCount() > 0) {
            output.append("\nFailures: ");
            List<Failure> failures = result.getFailures();
            for (int i = 0; i < failures.size(); i++) {
                Failure failure = failures.get(i);
                output.append("\n").append(i + 1).append(") ").append(failure);
            }
        }
        output.append("\nIgnored count: ").append(result.getIgnoreCount()).append("\n\n");
        return output;
    }
}
