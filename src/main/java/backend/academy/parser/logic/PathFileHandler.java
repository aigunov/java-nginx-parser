package backend.academy.parser.logic;

import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathFileHandler implements FileHandler {
    private final LogParser logParser = new LogParser();
    @Override
    public List<Log> handleFile(Filter filter) throws IOException {
        List<Path> paths = new ArrayList<>();
        for (var path : filter.path()) {
            paths.addAll(getPathsToFile(path, filter.domenPath()));
        }

        return paths.stream()
            .flatMap(this::parseFileLines)
            .map(logParser::parseLine)
            .collect(Collectors.toList());
    }

    private Stream<String> parseFileLines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + path);
            return Stream.empty();
        }
    }

    public List<Path> getPathsToFile(final String pattern, final Path rootDir) throws IOException {
        List<Path> matchesList = new ArrayList<>();
        FileVisitor<Path> matchesVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) throws IOException {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher(pattern);
                Path name = file.getFileName();
                if (matcher.matches(name)) {
                    matchesList.add(name);
                }
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(rootDir, matchesVisitor);
        return matchesList;
    }
}
