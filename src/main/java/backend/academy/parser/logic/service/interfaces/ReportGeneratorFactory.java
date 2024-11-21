package backend.academy.parser.logic.service.interfaces;

/**
 * Фабрика создания генераторов отчета
 */
public interface ReportGeneratorFactory {
    default ReportGenerator getReportGenerator() {
        return createReportGenerator();
    }

    /**
     * Фабричный метод
     */
    ReportGenerator createReportGenerator();
}
