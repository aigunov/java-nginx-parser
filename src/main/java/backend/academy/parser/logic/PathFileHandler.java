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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathFileHandler implements FileHandler {
    private final LogParser logParser = new LogParser();
    @Override
    public List<Log> handleFiles(Filter filter) {
        List<Path> paths = new ArrayList<>();
        for (var path : filter.paths()) {
            paths.addAll(getPathsToFile(path, filter.domenPath()));
        }
        return paths.stream()
            .flatMap(this::parseFileLines)
            .map(logParser::parseLine)
            .toList();
    }

    private Stream<String> parseFileLines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + path);
            return Stream.empty();
        }
    }

    private Set<Path> getPathsToFile(final String pattern, final Path rootDir) {
        Set<Path> matchesList = new TreeSet<>();
        FileVisitor<Path> matchesVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
                Path name = file.getFileName();
                if (matcher.matches(name)) {
                    matchesList.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(rootDir, matchesVisitor);
        } catch (IOException e) {
            log.error("Не удалось прочитать файл");
            throw new RuntimeException(e);
        }
        return matchesList;
    }
}
