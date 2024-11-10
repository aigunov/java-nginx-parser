package backend.academy;

import backend.academy.parser.LogAnalyzer;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        LogAnalyzer analyser = new LogAnalyzer();
        analyser.analyze(args, Paths.get("").toAbsolutePath().toString());
    }
}
