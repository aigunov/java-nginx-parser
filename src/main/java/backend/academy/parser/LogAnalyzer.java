package backend.academy.parser;

import backend.academy.parser.logic.PathFileHandler;
import backend.academy.parser.logic.URLFileHandler;
import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.model.Filter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class LogAnalyzer {
    private FileHandler handler;

    public void analyze(String[] args, String currentDirectory) {
        var filters = acceptCommand(args, currentDirectory);
        handler = determinateTypeOfFiles(filters.path());
        try {
            handler.handleFile(filters);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Filter acceptCommand(String[] args, String currentDirectory) {
        Filter filter = new Filter();
        JCommander commander = JCommander.newBuilder()
            .addObject(filter)
//            .addCommand("analyzer", filter)
            .build();
        filter.domenPath(Path.of(currentDirectory));
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
        return filter;
    }

    private FileHandler determinateTypeOfFiles(List<String> paths) {
        FileHandler filer;
        try{
            URI.create(paths.getFirst()).toURL();
            filer = new URLFileHandler();
        } catch (MalformedURLException e) {
            filer = new PathFileHandler();
        }
        return filer;
    }


}
