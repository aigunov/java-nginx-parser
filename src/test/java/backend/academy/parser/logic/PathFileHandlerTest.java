package backend.academy.parser.logic;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.assertj.core.api.Assertions.assertThat;

class PathFileHandlerTest {
    private PathFileHandler pathFileHandler;
    private Filter filter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        filter = new Filter();
        filter.paths(List.of("*.log"));
        filter.domenPath(tempDir);
        pathFileHandler = new PathFileHandler(filter);
    }

    @Test
    void testGetPathsToFile_WithMatchingFiles() throws IOException {
        Path matchingFile1 = Files.createFile(tempDir.resolve("test1.log"));
        Path matchingFile2 = Files.createFile(tempDir.resolve("test2.log"));
        Path nonMatchingFile = Files.createFile(tempDir.resolve("test.txt"));

        Stream<Path> result = pathFileHandler.getPathsToFile("*.log", tempDir);

        assertThat(result).containsExactlyInAnyOrder(matchingFile1, matchingFile2);
    }

    @Test
    void testGetPathsToFile_NoMatchingFiles() throws IOException {
        Files.createFile(tempDir.resolve("test.txt"));

        Stream<Path> result = pathFileHandler.getPathsToFile("*.log", tempDir);

        assertThat(result).isEmpty();
    }

    @Test
    void testReadFileLines_WithExistingFile() throws IOException {
        Path testFile = Files.createFile(tempDir.resolve("test.log"));
        Files.writeString(testFile, "line1\nline2\nline3");

        Stream<String> result = pathFileHandler.readFileLines(testFile);

        assertThat(result).containsExactly("line1", "line2", "line3");
    }

    @Test
    void testReadFileLines_FileDoesNotExist() {
        Path nonExistentFile = tempDir.resolve("nonexistent.log");

        Stream<String> result = pathFileHandler.readFileLines(nonExistentFile);

        assertThat(result).isEmpty();
    }

    @Test
    void testHandleFiles_WithValidFilter() throws IOException {
        Path testFile1 = Files.createFile(tempDir.resolve("test1.log"));
        Path testFile2 = Files.createFile(tempDir.resolve("test2.log"));
        Files.writeString(testFile1, "216.46.173.126 - - [04/Jun/2015:04:06:11 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.17)\"\n");
        Files.writeString(testFile2, "216.46.173.126 - - [04/Jun/2015:05:06:21 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.17)\"\n");


        Statistic result = pathFileHandler.handleFiles();

        assertThat(result).isNotNull();
        assertThat(result.requestCount()).isEqualTo(2);
    }
}
