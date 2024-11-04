package backend.academy;

import backend.academy.parser.LogAnalyzer;
import lombok.experimental.UtilityClass;
import java.util.Arrays;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        LogAnalyzer analyser = new LogAnalyzer();
        analyser.analyze(args);
    }
}
