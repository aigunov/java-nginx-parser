package backend.academy;

import backend.academy.parser.LogAnalyzer;
import lombok.experimental.UtilityClass;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@UtilityClass
public class Main {
    public static void main(String[] args) throws IOException {
        LogAnalyzer analyser = new LogAnalyzer();
        String currentDirectory = Paths.get("").toAbsolutePath().toString();
        System.out.println("Current directory: " + currentDirectory);
        analyser.analyze(args, currentDirectory);
    }
}
