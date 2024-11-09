package backend.academy.parser.logic.interfaces;

import backend.academy.parser.model.Filter;
import backend.academy.parser.model.Statistic;
import java.io.PrintStream;

public interface ReportGenerator {

    void generateReport(final Filter filter, final Statistic statistic, PrintStream out);
}
