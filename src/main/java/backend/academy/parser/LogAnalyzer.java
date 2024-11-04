package backend.academy.parser;

import backend.academy.parser.model.Filter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class LogAnalyzer {
    public void analyze(String[] args) {
        acceptCommand(args);
    }

    public void acceptCommand(String[] args) {
        Filter filter = new Filter();
        JCommander commander = JCommander.newBuilder()
            .addObject(filter)
//            .addCommand("analyzer", filter)
            .build();

        try {
            commander.parse(args);
            // Получаем объект filter с аргументами
            System.out.println("Path: " + filter.path());
            System.out.println("From: " + filter.from());
            System.out.println("To: " + filter.to());
            System.out.println("Format: " + filter.format());
            System.out.println("Filter Field: " + filter.filterField());
            System.out.println("Filter Value: " + filter.filterValue());
        } catch (ParameterException e) {
            System.err.println("Ошибка в аргументах: " + e.getMessage());
            commander.usage();
        }
    }
}
