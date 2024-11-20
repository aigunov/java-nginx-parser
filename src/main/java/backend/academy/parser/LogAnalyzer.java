package backend.academy.parser;

import backend.academy.parser.logic.service.implementations.ADOCReportGeneratorFactory;
import backend.academy.parser.logic.service.implementations.MDReportGeneratorFactory;
import backend.academy.parser.logic.service.implementations.PathFileHandlerFactory;
import backend.academy.parser.logic.service.implementations.URLFileHandlerFactory;
import backend.academy.parser.model.Filter;
import backend.academy.parser.model.ReportFormat;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintStream;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@SuppressFBWarnings({"DM_DEFAULT_ENCODING"})
@Slf4j
public class LogAnalyzer {
    private final PrintStream out = new PrintStream(System.out);

    public void analyze(String[] args, String currentDirectory) {
        var filter = acceptCommand(args, currentDirectory);
        var handlerFactory = filter.paths().getFirst().startsWith("http")
            ? new URLFileHandlerFactory() : new PathFileHandlerFactory();
        var handler = handlerFactory.getFileHandler(filter);
        var stats = handler.handleFiles();
        var reportGeneratorFactory = filter.format() == ReportFormat.MARKDOWN || filter.format() == null
            ? new MDReportGeneratorFactory() : new ADOCReportGeneratorFactory();
        var reportGenerator = reportGeneratorFactory.createReportGenerator();
        reportGenerator.generateReport(filter, stats, out);
    }

    public Filter acceptCommand(String[] args, String currentDirectory) {
        Filter filter = new Filter();
        JCommander commander = JCommander.newBuilder()
            .addObject(filter)
            .build();
        filter.domenPath(Path.of(currentDirectory));
        try {
            commander.parse(args);
        } catch (ParameterException e) {
            log.error("Ошибка в аргументах: {}", e.getMessage());
            commander.usage();
        }
        return filter;
    }

}
