package backend.academy.parser.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathFileHandlerTest {
    private Path tempDir;
    private PathFileHandler pathFileHandler;

    @BeforeEach
    public void setUp() throws IOException {
        pathFileHandler = new PathFileHandler();
        tempDir = Files.createDirectory(Path.of("C:\\Users\\Mr.White\\Desktop\\dev\\log-anal\\testDir"));

        Files.createFile(tempDir.resolve("testFile1.txt"));
        Files.createFile(tempDir.resolve("testFile2.txt"));

        var dir1 = Files.createDirectory(tempDir.resolve("dir1"));
        Files.createFile(dir1.resolve("dir1log1.txt"));
        Files.createFile(dir1.resolve("dir1log2.txt"));

    }

    @AfterEach
    public void tearDown() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                .sorted((path1, path2) -> path2.compareTo(path1)) // Сначала удаляем файлы, затем директории
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace(); // Обработка исключений
                    }
                });
        }
    }

    @Test
    public void testGetPathsToFile_MatchTxtFiles() throws IOException {
        String pattern = "glob:*.txt";
        List<Path> result = pathFileHandler.getPathsToFile(pattern, tempDir);

        assertEquals(4, result.size(), "Should find 4 .txt files in all directories");
        assertTrue(result.contains(tempDir.resolve("testFile1.txt").getFileName()));
        assertTrue(result.contains(tempDir.resolve("testFile2.txt").getFileName()));
        assertTrue(result.contains(tempDir.resolve("dir1/dir1log1.txt").getFileName()));
        assertTrue(result.contains(tempDir.resolve("dir1/dir1log2.txt").getFileName()));
    }

    @Test
    public void testGetPathsToFile_NoMatchForPattern() throws IOException {
        String pattern = "glob:*.log";
        List<Path> result = pathFileHandler.getPathsToFile(pattern, tempDir);

        assertTrue(result.isEmpty(), "No files should match the .log pattern");
    }

    @Test
    public void testGetPathsToFile_SpecificDirectoryOnly() throws IOException {
        String pattern = "glob:dir1*.txt";
        Path specificDir = tempDir.resolve("dir1");
        List<Path> result = pathFileHandler.getPathsToFile(pattern, specificDir);

        assertEquals(2, result.size(), "Should find only 2 .txt files in dir1");
        assertTrue(result.contains(specificDir.resolve("dir1log1.txt").getFileName()));
        assertTrue(result.contains(specificDir.resolve("dir1log2.txt").getFileName()));
    }
}
