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

}
