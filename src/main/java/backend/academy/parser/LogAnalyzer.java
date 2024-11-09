package backend.academy.parser;

import backend.academy.parser.logic.PathFileHandler;
import backend.academy.parser.logic.StatisticsCounter;
import backend.academy.parser.logic.URLFileHandler;
import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class LogAnalyzer {
    private StatisticsCounter counter;
    public void analyze(String[] args, String currentDirectory) {
        var filters = acceptCommand(args, currentDirectory);
        var handler = determinateTypeOfFiles(filters.paths().getFirst());
        var logs = handler.handleFiles(filters);
        counter = new StatisticsCounter(filters, logs);
        counter.countStatistic();

    }

    public Filter acceptCommand(String[] args, String currentDirectory) {
        Filter filter = new Filter();
        JCommander commander = JCommander.newBuilder()
            .addObject(filter)
            .build();
        filter.domenPath(Path.of(currentDirectory));
        try {
            commander.parse(args);
            // Получаем объект filter с аргументами
            System.out.println("Path: " + filter.paths());
            System.out.println("From: " + filter.from());
            System.out.println("To: " + filter.to());
            System.out.println("Format: " + filter.format());
            System.out.println("Filter Field: " + filter.filterField());
            System.out.println("Filter Value: " + filter.filterValue());
        } catch (ParameterException e) {
            System.err.println("Ошибка в аргументах: " + e.getMessage());
            commander.usage();
        }
        return filter;
    }

    private FileHandler determinateTypeOfFiles(final String path) {
        if (path.startsWith("http")) {
            return new URLFileHandler();
        } else {
            return new PathFileHandler();
        }
    }


}
