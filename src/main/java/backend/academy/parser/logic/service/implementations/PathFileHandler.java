package backend.academy.parser.logic.service.implementations;

import backend.academy.parser.logic.service.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
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
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * Класс работающий с локальными файлами
 */
@SuppressWarnings({"MultipleStringLiterals"})
@Slf4j
public class PathFileHandler extends FileHandler {
    private final static String WINDOWS_REGEX = ".*[\\\\/:*?\"<>|].*";
    private final static String MAC_REGEX = ".*[:].*";
    private final static String LINUX_REGEX = ".*[\\/].*";

    public PathFileHandler(final Filter filter) {
        super(filter);
    }

    @Override
    public Statistic handleFiles() {
        filter.paths().stream()
            .flatMap(path -> getPathsToFile(path, filter.domenPath()))
            .flatMap(this::readFileLines)
            .map(parser::parseLine)
            .filter(counter::filterLog)
            .forEach(counter::calculateDataInLog);

        return counter.getStatistic();
    }

    public Stream<String> readFileLines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            log.error("Ошибка чтения файла: {}", path);
            return Stream.empty();
        }
    }

    private boolean containsForbiddenChars(String filename) {
        return filename.matches(MAC_REGEX) || filename.matches(LINUX_REGEX) || filename.matches(WINDOWS_REGEX);
    }

    private String transformMethod(Path absolutePath, String relative) {
        String parentDirectoryMarker = "..";

        Path currentPath = absolutePath;

        var relatives = relative.split("\\\\");
        for (int i = 0; i < relatives.length; i++) {
            if (containsForbiddenChars(relatives[i])) {
                return currentPath.toString() + "\\"
                    + String.join("\\", Arrays.copyOfRange(relatives, i, relatives.length));
            }
            if (parentDirectoryMarker.equals(relatives[i])) {
                Path parentPath = currentPath.getParent();
                if (parentPath == null) {
                    throw new IllegalStateException("Cannot navigate above root directory.");
                }
                currentPath = parentPath;
            } else {
                currentPath = currentPath.resolve(relatives[i]);
            }
        }
        return currentPath.toString();
    }

    /**
     * Метод находит абсолютные пути к файлам соответствующие GLOB в pattern при помощи {@code FileVisitor<Path>}
     *
     * @param pattern - GLOB выражение по которому происходит  PathMatcher
     * @param rootDir - Корневая директория содержащие местоположение в системе места откуда была запущена программа
     * @return - Список всех абсолютных путей соответствующих glob выражению
     */
    public Stream<Path> getPathsToFile(final String pattern, Path rootDir) {
        Set<Path> matches = new TreeSet<>();
        var normalPattern = transformMethod(rootDir, pattern).replaceAll("\\\\", "/");
        FileVisitor<Path> matchesVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher("glob:" + normalPattern);
                Path name = file.getFileName();
                if (matcher.matches(name) || matcher.matches(file)) {
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
        return matches.stream();
    }
}
