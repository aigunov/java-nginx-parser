package backend.academy.parser.logic.service.implementations;

import backend.academy.parser.logic.service.interfaces.ReportGenerator;
import backend.academy.parser.logic.service.interfaces.ReportGeneratorFactory;

public class MDReportGeneratorFactory implements ReportGeneratorFactory {
    @Override
    public ReportGenerator createReportGenerator() {
        return new MDReportGenerator();
    }
}
