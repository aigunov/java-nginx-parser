package backend.academy.parser.logic.service.interfaces;

public interface ReportGeneratorFactory {
    default ReportGenerator getReportGenerator() {
        return createReportGenerator();
    }

    ReportGenerator createReportGenerator();
}
