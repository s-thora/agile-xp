package com.estimator.compiler;

import com.estimator.compiler.exception.CompilationFailedException;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Compiler {

    public static void compile(List<Path> filePaths, Path outDir) throws CompilationFailedException {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(getFiles(filePaths));
            final Iterable<String> options = Arrays.asList("-d", outDir.toString(), "-Xdiags:verbose");
            compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call();
            if (!diagnostics.getDiagnostics().isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    message.append(getMessage(diagnostic));
                }
                throw new CompilationFailedException(message.toString());
            }

            fileManager.close();
        } catch (IOException io) {
            io.printStackTrace();
            throw new CompilationFailedException(io.getMessage());
        }
    }

    private static String getMessage(Diagnostic diagnostic) {
        String error;
        if (diagnostic.getSource() != null) {
            error = String.format("Error on line %d in %s:\n %s\n\n",
                    diagnostic.getLineNumber(),
                    getFilename(diagnostic.getSource().toString()),
                    diagnostic.getMessage(Locale.ENGLISH));
        } else {
            error = String.format("Error:\n %s\n\n",
                    diagnostic.getMessage(Locale.ENGLISH));
        }
        return error;
    }

    private static String getFilename(String path) {
        return path.substring(path.lastIndexOf('\\') + 1, path.length()-1);
    }

    private static List<File> getFiles(List<Path> filePaths) {
        return filePaths.stream().map(Path::toFile).collect(Collectors.toList());
    }

}
