package backend.academy.parser;

import backend.academy.parser.logic.PathFileHandler;
import backend.academy.parser.logic.StatisticsCounter;
import backend.academy.parser.logic.URLFileHandler;
import backend.academy.parser.logic.interfaces.FileHandler;
import backend.academy.parser.logic.reports.ADOCReportGenerator;
import backend.academy.parser.logic.reports.MDReportGenerator;
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
        var filters = acceptCommand(args, currentDirectory);
        var handler = determinateTypeOfFiles(filters.paths().getFirst(), filters);
        var stats = handler.handleFiles();
        var reportGenerator =
            filters.format() == ReportFormat.MARKDOWN || filters.format() == null
                ? new MDReportGenerator() : new ADOCReportGenerator();
        reportGenerator.generateReport(filters, stats, out);
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

    private FileHandler determinateTypeOfFiles(final String path, final Filter filters) {
        if (path.startsWith("http")) {
            return new URLFileHandler(StatisticsCounter.getInstance(), filters);
        } else {
            return new PathFileHandler(StatisticsCounter.getInstance(), filters);
        }
    }
}
