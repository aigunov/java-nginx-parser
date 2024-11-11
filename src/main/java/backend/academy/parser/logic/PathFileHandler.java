package backend.academy.parser.logic;

import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
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
import backend.academy.parser.model.Statistic;
import lombok.extern.slf4j.Slf4j;

/**
 * Класс работающий с локальными файлами
 */
@Slf4j
public class PathFileHandler implements FileHandler {
    private final StatisticsCounter counter;
    private final Filter filter;

    public PathFileHandler(StatisticsCounter counter, Filter filter) {
        this.counter = counter;
        this.filter = filter;
    }

    @Override
    public Statistic handleFiles() {
        List<Path> paths = new ArrayList<>();
        for (var path : filter.paths()) {
            paths.addAll(getPathsToFile(path, filter.domenPath()));
        }
        paths.stream()
            .flatMap(this::parseFileLines)
            .map(LogParser::parseLine)
            .filter(log -> counter.filterLog(log, filter))
            .forEach(counter::calculateDataInLog);

        return counter.getStatistic();
    }

    Stream<String> parseFileLines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            log.error("Ошибка чтения файла: {}", path);
            return Stream.empty();
        }
    }

    /**
     * Метод находит абсолютные пути к файлам соответствующие GLOB в pattern при помощи {@code FileVisitor<Path>}
     *
     * @param pattern - GLOB выражение по которому происходит  PathMatcher
     * @param rootDir - Корневая директория содержащие местоположение в системе места откуда была запущена программа
     * @return - Список всех абсолютных путей соответствующих glob выражению
     */
    Set<Path> getPathsToFile(final String pattern, final Path rootDir) {
        Set<Path> matches = new TreeSet<>();
        FileVisitor<Path> matchesVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
                Path name = file.getFileName();
                if (matcher.matches(name)) {
                    matches.add(file);
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
        return matches;
    }
}
