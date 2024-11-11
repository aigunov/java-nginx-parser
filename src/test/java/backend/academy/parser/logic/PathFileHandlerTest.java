package backend.academy.parser.logic;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Log;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathFileHandlerTest {

//    private Path tempDir;
//    private PathFileHandler pathFileHandler;
//    private LogParser logParser;
//
//    @BeforeEach
//    public void setUp() throws IOException {
//        tempDir = Files.createTempDirectory("testDir");
//        Filter mock = Mockito.mock(Filter.class);)
//        pathFileHandler = new PathFileHandler(StatisticsCounter.getInstance(), mock);
//        logParser = new LogParser();
//    }
//
//    @AfterEach
//    public void tearDown() throws IOException {
//        Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                Files.delete(file);
//                return FileVisitResult.CONTINUE;
//            }
//
//            @Override
//            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                Files.delete(dir);
//                return FileVisitResult.CONTINUE;
//            }
//        });
//    }
//
//    @Test
//    public void testGetPathsToFile_withMatchingFiles() throws IOException {
//        Path matchingFile1 = Files.createFile(tempDir.resolve("logfile1.log"));
//        Path matchingFile2 = Files.createFile(tempDir.resolve("logfile2.log"));
//        Path nonMatchingFile = Files.createFile(tempDir.resolve("otherfile.txt"));
//
//        Set<Path> result = pathFileHandler.getPathsToFile("*.log", tempDir);
//
//        assertTrue(result.contains(matchingFile1), "Ожидается, что файл logfile1.log будет найден");
//        assertTrue(result.contains(matchingFile2), "Ожидается, что файл logfile2.log будет найден");
//        assertFalse(result.contains(nonMatchingFile), "Ожидается, что файл otherfile.txt не будет найден");
//        assertEquals(2, result.size(), "Ожидается, что найдено ровно 2 файла");
//    }
//
//    @Test
//    public void testParseFileLines_withValidFile() throws IOException {
//        Path testFile = Files.createFile(tempDir.resolve("logfile.log"));
//        Files.writeString(testFile, "Log entry 1\nLog entry 2\n");
//
//        Stream<String> lines = pathFileHandler.parseFileLines(testFile);
//        List<String> result = lines.toList();
//
//        assertEquals(2, result.size(), "Ожидается 2 строки");
//        assertEquals("Log entry 1", result.get(0));
//        assertEquals("Log entry 2", result.get(1));
//    }
//
//    @Test
//    public void testParseFileLines_withNonExistentFile() {
//        Path nonExistentFile = tempDir.resolve("nonexistent.log");
//
//        Stream<String> lines = pathFileHandler.parseFileLines(nonExistentFile);
//
//        assertTrue(lines.toList().isEmpty(), "Ожидается пустой поток строк для несуществующего файла");
//    }
//
//    @Test
//    public void testHandleFiles_withMultipleFiles() throws IOException {
//        Path logFile1 = Files.createFile(tempDir.resolve("logfile1.log"));
//        Files.writeString(logFile1,
//            "216.46.173.126 - - [04/Jun/2015:04:06:36 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.17)\"\n");
//        Path logFile2 = Files.createFile(tempDir.resolve("logfile2.log"));
//        Files.writeString(logFile2,
//            "117.203.36.5 - - [04/Jun/2015:04:06:17 +0000] \"GET /downloads/product_2 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3 (1.0.1ubuntu2)\"\n");
//        Filter filter = Filter.builder()
//            .paths(List.of("*.log"))
//            .domenPath(tempDir)
//            .build();
//
//        List<Log> logs = pathFileHandler.handleFiles(filter);
//
//        assertEquals(2, logs.size(), "Ожидается, что найдено и обработано 2 записи лога");
//    }
}
